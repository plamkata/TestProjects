using System;
using System.Data;
using System.Data.SqlClient;
using PhotoAlbumDB.Exceptions;

namespace PhotoAlbumDB.Entities
{
	/// <summary>
	/// This is an object representation of our 
	/// bussiness entity - Image.
	/// </summary>
	public class Image : DBFile
	{
		private Int32 id = -1;
		/// <summary>
		/// This field is only a holder of the parent category 
		/// when creating new image. After calling save() it 
		/// is set to null.
		/// </summary>
		private Category parentCategory;
		/// <summary>
		/// Keeps the DBConnection object.
		/// </summary>
		private User user;
		private String fileName;
		private String realFileName;
		private String owner;
		private DateTime dateModified;
		private String description;
		private String format;
		private Int64 size = 0;
		private bool shared = false;

		#region DBFile Members
		/// <summary>
		/// The name of the image.
		/// Requires update() after being changed in 
		/// order to take effect in DB.
		/// </summary>
		public String Name
		{
			get
			{
				return fileName;
			}
			set
			{
				this.fileName = value;
			}
		}

		public Int64 Size
		{
			get
			{
				return size;
			}
			set 
			{
				size = value;
			}
		}

		public DateTime DateModified
		{
			get
			{
				return dateModified;
			}
			set 
			{
				dateModified = value;
			}
		}

		public bool Shared
		{
			get
			{
				// should first load the changes made 
				// when getting this prop.
				try 
				{
					SqlCommand cmd = new SqlCommand();
					cmd.Connection = user.Connection.getInstance();
					load(id, cmd);
				} 
				catch (SqlException e) 
				{	
					throw new ServerInfoException(
						e.Message, user.Connection.ServerInfos, e);	
				} 
				catch (Exception e) 
				{
					if (e is ServerInfoException) throw e;
					else 
					{
						throw new ObjectDBException(
							"Problem loading image changes.", this, e);
					}
				}
				return shared;
			}
			set
			{
				// sharing is one time operation
				// cannot revert
				if (value == true) 
				{
					if (id == -1 || user.ID == -1)
						throw new ObjectDBException("Image not present!", this);
					try 
					{
						SqlCommand cmd = new SqlCommand(
							"sp_ShareImage", 
							user.Connection.getInstance()
						);
						cmd.CommandType = CommandType.StoredProcedure;
						cmd.Parameters.Add("@Image_Id", id);
						cmd.Parameters.Add("@User_Id", user.ID);

						cmd.ExecuteNonQuery();
						shared = true;
					} 
					catch (SqlException e) 
					{	
						throw new ServerInfoException(
							e.Message, user.Connection.ServerInfos, e);	
					} 
					catch (Exception e) 
					{
						if (e is ServerInfoException) throw e;
						else 
						{
							throw new ObjectDBException(
								"Problem sharing image.", this, e);
						}
					}
				}
			}
		}

		public String Owner
		{
			get
			{
				return owner;
			}
		}

		public void view(ImageViewer viewer, bool isThumbnail) 
		{
			if (isThumbnail) 
			{
				viewer.viewThumbnail(this);
			} 
			else 
			{
				viewer.viewImage(this);
			}
		}
		#endregion

		#region DBObject Members

		public void save()
		{
			if (this.id >= 0 || parentCategory == null || realFileName == null) 
			{
				// Image should not have an id to be inserted
				throw new ObjectDBException("Problem creating image!", this);
			} 
			// the user of the new image is the 
			// same as the user of the parent category
			SqlTransaction trans = null;
			try 
			{
				trans = user.Connection.getInstance().
					BeginTransaction(IsolationLevel.Serializable);
				SqlCommand cmd = new SqlCommand();

				cmd.Connection = user.Connection.getInstance();
				cmd.Transaction = trans;
				cmd.CommandType = CommandType.StoredProcedure;
				FileManger.saveImage(cmd, this, realFileName);
				trans.Commit();
			} 
			catch (SqlException e) 
			{	
				if (trans != null) trans.Rollback();
				throw new ServerInfoException(
					e.Message, user.Connection.ServerInfos, e);	
			} 
			catch (Exception e) 
			{
				if (trans != null) trans.Rollback();
				if (e is ServerInfoException) throw e;
				else 
				{
					throw new ObjectDBException("Problem creating new category!", this, e);
				}

			}
			parentCategory = null;
			realFileName = null;
			shared = false;
		}

		public void delete()
		{
			if (this.id == -1) 
			{
				// Image should have an id to be deleted
				throw new ObjectDBException("Problem deleting image!", this);
			} 
			try 
			{
				SqlCommand cmd = new SqlCommand(
					"sp_DeleteImage", 
					user.Connection.getInstance()
				);
				cmd.CommandType = CommandType.StoredProcedure;
				cmd.Parameters.Add("@Image_Id", id);
				cmd.Parameters.Add("@User_Id", user.ID);
				
				cmd.ExecuteNonQuery();
				id = -1;
			} 
			catch (SqlException e) 
			{	
				throw new ServerInfoException(
					e.Message, user.Connection.ServerInfos, e);	
			} 
			catch (Exception e) 
			{
				if (e is ServerInfoException) throw e;
				else 
				{
					throw new ObjectDBException("Problem deleting image.", this, e);
				}
			}
		}

		public void update()
		{
			// update image name and description
			if (id == -1 || user.ID == -1)
				throw new ObjectDBException("Image not present!", this);
			try 
			{
				SqlCommand cmd = new SqlCommand(
					"sp_ChangeImageInfo", 
					user.Connection.getInstance()
				);
				cmd.CommandType = CommandType.StoredProcedure;
				cmd.Parameters.Add("@Image_Id", id);
				cmd.Parameters.Add("@New_Name", fileName);
				cmd.Parameters.Add("@New_Description", description);
				cmd.Parameters.Add("@User_Id", user.ID);

				cmd.ExecuteNonQuery();
			} 
			catch (SqlException e) 
			{	
				throw new ServerInfoException(
					e.Message, user.Connection.ServerInfos, e);	
			} 
			catch (Exception e) 
			{
				if (e is ServerInfoException) throw e;
				else 
				{
					throw new ObjectDBException(
						"Problem changing image info.", this, e);
				}
			}
		}

		#endregion

		public string Description 
		{
			get 
			{
				return description;
			}
			set 
			{
				this.description = value;
			}
		}		

		public byte[] Binary 
		{
			get 
			{
				if (this.id < 0 || this.user == null) 
				{
					// Image should have an id 
					throw new ObjectDBException("Problem creating image!", this);
				} 
				byte[] result = new byte[0];
				SqlTransaction trans = null; 
				try 
				{
					trans = user.Connection.getInstance().BeginTransaction();
					SqlCommand cmd = new SqlCommand();
					cmd.Connection = user.Connection.getInstance();
					cmd.Transaction = trans;
					cmd.CommandType = CommandType.StoredProcedure;
					cmd.CommandText = "sp_getImage";
					cmd.Parameters.Add("@Image_Id", id);

					SqlDataReader reader = cmd.ExecuteReader();
					if (reader.Read())
					{
						result = (byte[])reader["image"];
					}
					reader.Close();
					trans.Commit();
				} 
				catch (SqlException e) 
				{	
					if (trans != null) trans.Rollback();
					throw new ServerInfoException(
						e.Message, user.Connection.ServerInfos, e);	
				} 
				catch (Exception e) 
				{
					if (trans != null) trans.Rollback();
					if (e is ServerInfoException) throw e;
					else 
					{
						throw new ObjectDBException("Problem creating new category!", this, e);
					}

				}
				return result;
			}
		}
		internal Category ParentCategory 
		{
			get 
			{
				return parentCategory;
			}
		}


		internal User CurrentUser
		{
			get
			{
				return user;
			}
		}
		internal Int32 ID 
		{
			set 
			{
				id = value;
			}
		}
		public Image(String fileName, Category parentCategory)
		{
			this.realFileName = fileName;
			this.parentCategory = parentCategory;			
			this.user = parentCategory.User;
		}

		/// <summary>
		/// Loads a new existing image by its id.
		/// Since this is internal constructor it 
		/// dosen't wrap the exceptions(i.e. danegrouse code). 
		/// </summary>
		/// <param name="categoryId">the id of the image</param>
		/// <param name="cmd">the command object to be used
		/// (convinient when using transactions)</param>
		/// <param name="user">the current user of this image</param>
		internal Image(Int32 imageId, SqlCommand cmd, User user) 
		{
			this.user = user;
			load(imageId, cmd);
		}

		/// <summary>
		/// Dosen't wrap the exceptions(i.e. danegrouse code). 
		/// To be used only in this class.
		/// </summary>
		/// <param name="id">the identifier of the current image</param>
		/// <param name="cmd">the <code>SqlComand</code> to be used</param>
		protected void load(Int32 id, SqlCommand cmd) 
		{
			cmd.CommandText = "sp_GetImageInfo";
			cmd.CommandType = CommandType.StoredProcedure;
			cmd.Parameters.Clear();
			cmd.Parameters.Add("@Image_Id", id);

			SqlDataReader reader = cmd.ExecuteReader();
			if (reader.Read())
			{
				fileName = (string)reader["name"];
				size = (Int64)reader["size"];
				dateModified = (DateTime)reader["date_modified"]; 
				description = (string)reader["description"];
				shared = (bool)reader["shared"];
				format = (string)reader["format"];
				owner = (string)reader["owner"];
			}
			reader.Close();
			this.id = id;
		}


		/// <summary>
		/// The interface from the Builder Pattern.
		/// </summary>
		public interface ImageViewer
		{
			/// <summary>
			/// This method is implemented by the container.
			/// The implemented method should show the given 
			/// picture to the user in its best size.
			/// </summary>
			/// <param name="image">the image to be displaied</param>
			void viewImage(Image image);

			/// <summary>
			/// This method is implemented by the container.
			/// The implemented method should show the given 
			/// picture to the user in a small size.
			/// </summary>
			/// <param name="image">the small thumbnail to be displayed</param>
			void viewThumbnail(Image image);
		}
	}
}
