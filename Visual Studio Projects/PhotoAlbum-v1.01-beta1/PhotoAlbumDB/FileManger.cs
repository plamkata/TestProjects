using System;
using System.IO;
using System.Data.SqlClient;
using System.Data;
using PhotoAlbumDB.Entities;
using PhotoAlbumDB.Exceptions;

namespace PhotoAlbumDB
{
	/// <summary>
	/// This class is used for the interactions with the file system.
	/// It is also responcible for part of the work with the DB.
	/// </summary>
	internal sealed class FileManger
	{
		/// <summary>
		/// transactional save for an image
		/// </summary>
		/// <param name="cmd">the SqlCommand to be used</param>
		/// <param name="img">the image to be saved should've been asigned
		///  a ParentCategory before using this method and should 
		///  not be present in the DB.</param>
		/// <param name="fileName">the image full path</param>
		internal static void saveImage(SqlCommand cmd, 
			Image img, string fileName) 
		{
			cmd.CommandText = "sp_CreateNewImage";
			cmd.Parameters.Clear();

			int lastSeparatorIndex = fileName.LastIndexOf(@"\");
			img.Name = fileName.Substring(lastSeparatorIndex + 1); 
			img.Description = fileName;

			cmd.Parameters.Add("@File_Name", img.Name);
				
			byte[] binary = FileManger.readBinaryFile(fileName);
			img.Size = binary.Length;
			SqlParameter binaryDataParam = new SqlParameter("@Image", binary);
			binaryDataParam.SqlDbType = SqlDbType.Image;
			cmd.Parameters.Add(binaryDataParam);
			cmd.Parameters.Add("@Size", img.Size);
				
			cmd.Parameters.Add("@Description", img.Description);
			cmd.Parameters.Add("@Format_Name", GetImageFormat(fileName));
			cmd.Parameters.Add("@Category_Id", img.ParentCategory.ID);
			cmd.Parameters.Add("@User_Id", img.CurrentUser.ID);

			SqlParameter imageIdParam = 
				cmd.Parameters.Add("@Image_Id", SqlDbType.Int);
			imageIdParam.Direction = ParameterDirection.Output;
			SqlParameter dateModifiedParam = 
				cmd.Parameters.Add("@Date_Modified", SqlDbType.DateTime);
			dateModifiedParam.Direction = ParameterDirection.Output;
				
			cmd.ExecuteNonQuery();
				
			img.ID = (Int32) imageIdParam.Value;
			img.DateModified = (DateTime)dateModifiedParam.Value;
		}

		/// <summary>
		/// transactional save for a category 
		/// </summary>
		/// <param name="cmd">the SqlCommand to be used</param>
		/// <param name="cat">the category to be saved should've been asigned
		///  a ParentCategory before using this method and should 
		///  not be present in the DB.</param>
		internal static void saveCategory(SqlCommand cmd, 
			Category cat) 
		{
			
			cmd.CommandText = "sp_CreateNewCategory";
			cmd.Parameters.Add("@Name", cat.Name);
			cmd.Parameters.Add("@Parent_Id", cat.ParentCategory.ID);
			cmd.Parameters.Add("@User_Id", cat.User.ID);
			SqlParameter categoryIdParam = 
				cmd.Parameters.Add("@Category_Id", SqlDbType.Int);
			categoryIdParam.Direction = ParameterDirection.Output;
			SqlParameter dateModifiedParam = 
				cmd.Parameters.Add("@Date_Modified", SqlDbType.DateTime);
			dateModifiedParam.Direction = ParameterDirection.Output;
				
			cmd.ExecuteNonQuery();
				
			cat.ID = (Int32) categoryIdParam.Value;
			cat.DateModified = (DateTime)dateModifiedParam.Value;
		}

		/// <summary>
		/// To be used with transactions.
		/// Saves the category given and all the images 
		/// inside the folder name.
		/// No deep loading.
		/// </summary>
		/// <param name="cmd">The command object asociated with a transaction.</param>
		/// <param name="cat">the category to be saved</param>
		/// <param name="folderName">the folder full path</param>
		internal static void saveSingleFolder(SqlCommand cmd, 
			Category cat, string folderName) 
		{
			int lastSeparatorIndex = folderName.LastIndexOf(@"\");
			cat.Name = folderName.Substring(lastSeparatorIndex + 1);
			saveCategory(cmd, cat);// transactional save
						
			string[] fileNames = getFileNames(folderName);
			if (fileNames.Length <= 0) 
			{
				// this will rowback the transaction
				throw new ObjectDBException("No images in this folder!", cat);
			}
			for (int i = 0; i < fileNames.Length; i++) 
			{
				Image img = new Image(fileNames[i], cat);
				saveImage(cmd, img, fileNames[i]);// transactional save
			}
			
		}

		/// <summary>
		/// To be used with transactions.
		/// Saves all the images inside the folder with this path-name
		/// under the given Category. No deep loading.
		/// </summary>
		/// <param name="cmd">The command object asociated with a transaction.</param>
		/// <param name="parent">the target category</param>
		/// <param name="folderName">the folder full path</param>
		internal static void loadFromFolder(SqlCommand cmd, 
			Category parent, string folderName, LoadInfoViewer viewer) 
		{			
			string[] fileNames = getFileNames(folderName);
			int count = 0;
			
			for (int i = 0; i < fileNames.Length; i++) 
			{
				Image img = new Image(fileNames[i], parent);
				viewer.Invoke(
					new ShowFilenameDlegate(viewer.showFileName), 
					new object[] {fileNames[i]}
				);
				saveImage(cmd, img, fileNames[i]);// transactional save
				count++;
				int percent = (count*100)/fileNames.Length;
				viewer.Invoke(
					new ShowProccessDlegate(viewer.showProccess), 
					new object[] {percent}
				);
				img = null;
			}
			fileNames = null;
			System.GC.Collect();
		}

		internal static void deepLoadFromFolder(SqlCommand cmd, 
			Category parent, string fileName, LoadInfoViewer viewer) 
		{
			string[] subDirs = Directory.GetDirectories(fileName);
			foreach (string subDir in subDirs) 
			{
				deepLoadFromFolder(cmd, parent, subDir, viewer);
			}
			loadFromFolder(cmd, parent, fileName, viewer);
		}

		private static string[] getFileNames(string directoryName) 
		{
			if (Directory.Exists(directoryName)) 
			{
				string[] jpgFiles = Directory.GetFiles(directoryName, "*.jpg");				
				string[] pngFiles = Directory.GetFiles(directoryName, "*.png");
				string[] gifFiles = Directory.GetFiles(directoryName, "*.gif");
				string[] allFiles = new string[jpgFiles.Length + 
					pngFiles.Length + gifFiles.Length];
				Array.Copy(jpgFiles, allFiles, jpgFiles.Length);
				Array.Copy(pngFiles, allFiles, pngFiles.Length);
				Array.Copy(gifFiles, allFiles, gifFiles.Length);
				return allFiles;
			}
			return new string[0];
		}

		private static byte[] readBinaryFile(string aFileName) 
		{
			byte[] buf;
			FileStream fs = File.OpenRead(aFileName);
			using (fs)
			{
				int pos = 0;
				int length = (int) fs.Length;
				buf = new byte[length];
				while (true)
				{
					int bytesRead = fs.Read(buf, pos, length-pos);
					if (bytesRead == 0)
					{
						break;
					}
					pos += bytesRead;
				}
			}
			return buf;
		}
			
		private static string GetImageFormat(string aFileName)
		{
			FileInfo fileInfo = new FileInfo(aFileName);
			string fileExtenstion = fileInfo.Extension;
			string imageFormat = fileExtenstion.ToLower().Substring(1);
			return imageFormat;
		}

	}
}
