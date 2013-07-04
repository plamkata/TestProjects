using System;
using System.Data;
using System.Data.SqlClient;
using System.Collections;
using System.Globalization;
using PhotoAlbumDB.Exceptions;

namespace PhotoAlbumDB.Entities
{
	/// <summary>
	/// This is an object representation of our 
	/// bussiness entity - Category.
	/// </summary>
	public class Category : DBFile
	{
		private Int32 id = -1;
		/// <summary>
		/// This field is only a holder of the parent category 
		/// when creating new category. After calling save() it 
		/// is set to null.
		/// </summary>
		private Category parent;
		private User user;
		private string categoryName;
		private string owner;
		private DateTime dateModified;
		private bool shared;

		private string folderName;
		private bool deep = false;
		private LoadInfoViewer mViewer;
		
		#region DBFile Members
	
		/// <summary>
		/// The name of the category.
		/// Requires update() after being changed in 
		/// order to take effect in DB.
		/// </summary>
		public string Name 
		{
			get 
			{
				return categoryName;
			}
			set 
			{
				categoryName = value;
			}
		}		
		
		public DateTime DateModified 
		{
			set 
			{
				dateModified = value;
			}
			get 
			{
				System.Threading.Thread.CurrentThread.CurrentCulture =
					CultureInfo.InvariantCulture;
				return dateModified;
			}
		}

		public String Owner 
		{
			get 
			{
				return owner;
			}
		}

		/// <summary>
		/// Shares this category and all its content.
		/// Heavy operation - i.e. dose its work with the DB alone. 
		/// </summary>
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
							"Problem loading category changes.", this, e);
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
						throw new ObjectDBException(
							"Category not present!", this);
					try 
					{
						SqlCommand cmd = new SqlCommand(
							"sp_ShareCategory", 
							user.Connection.getInstance()
						);
						cmd.CommandType = CommandType.StoredProcedure;
						cmd.Parameters.Add("@Category_Id", id);
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
								"Problem sharing category name.", this, e);
						}
					}
				}
			}
		}

		public Int64 Size 
		{
			get 
			{
				// call the stored procedure which calculates 
				//the size of this cotegory
				if (id == -1 || user.ID == -1)
					throw new ObjectDBException(
						"Category not present!", this);
				try 
				{
					SqlCommand cmd = new SqlCommand(
						"sp_GetSizeOfCategory", 
						user.Connection.getInstance()
					);
					cmd.CommandType = CommandType.StoredProcedure;
					cmd.Parameters.Add("@Category_Id", id);
					SqlParameter sizeParam = 
						cmd.Parameters.Add("@Size", SqlDbType.BigInt);
					sizeParam.Direction = ParameterDirection.Output;

					cmd.ExecuteNonQuery();

					return (Int64)sizeParam.Value;
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
							"Problem calculating category size.", this, e);
					}
				}
			}
		}
		
		public void view(Image.ImageViewer viewer, bool isThumbnail) 
		{
			if (this.id == -1) 
			{
				// Category should have an id to get all subimages
				throw new ObjectDBException("Problem retrieving SubImages!", this);
			} 
			IList subItems = new ArrayList(10);
			try 
			{
				SqlCommand cmd = new SqlCommand();
				cmd.Connection = user.Connection.getInstance();
				// view subimages
				IList imgIds = getSubImageIds(cmd);
				foreach (Int32 imgId in imgIds) 
				{
					DBFile imgItem = new Image(imgId, cmd, user);
					// it is always thumbnail
					imgItem.view(viewer, true);
				}
			} 
			catch (SqlException e) 
			{	
				string msg = e.Message;
				if (msg.IndexOf("\n") > 0) 
				{
					msg = msg.Substring(0, msg.IndexOf("\r\n"));
				}
				throw new ServerInfoException(
					msg, user.Connection.ServerInfos, e);
			} 
			catch (Exception e) 
			{
				if (e is ServerInfoException) throw e;
				else 
				{
					throw new ObjectDBException(
						"Problem retrieving SubImages!", this, e);
				}
			}
		}
		#endregion
			
		#region DBObject Members

		public void save()
		{
			if (this.id >= 0 || parent == null || categoryName == null) 
			{
				// Category should not have an id to be inserted
				throw new ObjectDBException("Problem creating category!", this);
			} 
			// the user of the new category is the 
			// same as the user of the parent category
			user = parent.user;
			try 
			{
				SqlCommand cmd = new SqlCommand();
				cmd.Connection = user.Connection.getInstance();
				// no transaction needed here
				cmd.CommandType = CommandType.StoredProcedure;
				FileManger.saveCategory(cmd, this);				
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
					throw new ObjectDBException("Problem creating new category!", this, e);
				}

			}
			parent = null;
			shared = false;
		}

		/// <summary>
		/// Explicitly call dispose after deleting.
		/// </summary>
		public void delete()
		{
			if (this.id == -1) 
			{
				// Category should have an id to be deleted
				throw new ObjectDBException("Problem deleting category!", this);
			} 
			try 
			{
				SqlCommand cmd = new SqlCommand(
					"sp_DeleteCategory", 
					user.Connection.getInstance()
					);
				cmd.CommandType = CommandType.StoredProcedure;
				cmd.Parameters.Add("@Category_Id", id);
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
					throw new ObjectDBException("Problem deleting category", this, e);
				}
			}
		}
		
		/// <summary>
		/// Updates only the name of the category.
		/// </summary>
		public void update()
		{
			// update category name
			if (id == -1 || user.ID == -1)
				throw new ObjectDBException("Category not present!", this);
			try 
			{
				SqlCommand cmd = new SqlCommand(
					"sp_ChangeCategoryName", 
					user.Connection.getInstance()
					);
				cmd.CommandType = CommandType.StoredProcedure;
				cmd.Parameters.Add("@Category_Id", id);
				cmd.Parameters.Add("@New_Name", categoryName);
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
						"Problem changing category name.", this, e);
				}
			}
		}

		#endregion
		/// <summary>
		/// A collection containing all DBItems 
		/// in the current Category.
		/// </summary>
		public IList SubItems 
		{
			get 
			{
				if (this.id == -1) 
				{
					// Category should have an id to get all subitems
					throw new ObjectDBException("Problem retrieving SubItems!", this);
				} 
				IList subItems = new ArrayList(10);
				SqlTransaction trans = null;
				try 
				{
					trans = user.Connection.getInstance().
						BeginTransaction(IsolationLevel.Serializable);
					SqlCommand cmd = new SqlCommand();
					cmd.Connection = user.Connection.getInstance();
					cmd.Transaction = trans;
					// add subcategories
					IList catIds = getSubCategoryIds(cmd);
					foreach (Int32 catId in catIds) 
					{
						DBFile catItem = new Category(catId, cmd, user);
						subItems.Add(catItem);
					}
					// add subimages
					IList imgIds = getSubImageIds(cmd);
					foreach (Int32 imgId in imgIds) 
					{
						DBFile imgItem = new Image(imgId, cmd, user);
						subItems.Add(imgItem);
					}
					trans.Commit();
				} 
				catch (SqlException e) 
				{	
					if (trans != null) trans.Rollback();
					string msg = e.Message;
					if (msg.IndexOf("\n") > 0) 
					{
						msg = msg.Substring(0, msg.IndexOf("\r\n"));
					}
					throw new ServerInfoException(
						msg, user.Connection.ServerInfos, e);
				} 
				catch (Exception e) 
				{
					if (trans != null) trans.Rollback();
					if (e is ServerInfoException) throw e;
					else 
					{
						throw new ObjectDBException(
							"Problem retrieving SubItems!", this, e);
					}

				}
				return subItems;
			}
		}

		internal Category ParentCategory 
		{
			get 
			{
				return parent;
			}
		}

		internal User User 
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
			get 
			{
				return id;
			}
		}
		public Category(Category parentCategory)
		{
			this.parent = parentCategory;
			this.user = parentCategory.User;
		}
		
		/// <summary>
		/// Creates new category from the file system.
		/// no save() is required after that.
		/// </summary>
		/// <param name="parentCategory">the parent</param>
		/// <param name="fileName">the full path</param>
		/// <param name="deep">determine if you will make a deep copy, 
		/// or you will just copy only the images in this category</param>
		public Category(Category parentCategory, String fileName, bool deep) 
		{
			if (parentCategory == null || fileName == null) 
			{
				throw new ObjectDBException("Problem creating category!", this);
			} 
			// the user of the new category is the 
			// same as the user of the parent category
			this.parent = parentCategory;
			user = parentCategory.User;
			SqlTransaction trans = null;
			try 
			{
				trans = user.Connection.getInstance().BeginTransaction();
				SqlCommand cmd = new SqlCommand();

				cmd.Connection = user.Connection.getInstance();
				cmd.Transaction = trans;
				cmd.CommandType = CommandType.StoredProcedure;
				if (!deep) 
				{
					FileManger.saveSingleFolder(cmd, this, fileName);
				} 
				else 
				{

				}
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
			shared = false;
		}
		/// <summary>
		/// Loads a new existing category by its id.
		/// Since this is internal constructor it 
		/// dosen't wrap the exceptions(i.e. danegrouse code). 
		/// </summary>
		/// <param name="categoryId">the id of the category</param>
		/// <param name="cmd">the command object to be used
		/// (convinient when using transactions)</param>
		/// <param name="user">the current user of this category</param>
		internal Category(Int32 categoryId, SqlCommand cmd, User user) 
		{
			this.user = user;
			load(categoryId, cmd);
		}


		public void prepareForLoad(String folderName, bool deep, LoadInfoViewer viewer) 
		{
			this.folderName = folderName;
			this.deep = deep;
			this.mViewer = viewer;
		}

		public void loadImagesFromFolder() 
		{
			if (user == null || folderName == null || mViewer == null) 
			{
				throw new ObjectDBException("Problem creating category!", this);
			} 
			SqlTransaction trans = null;
			try 
			{
				trans = user.Connection.getInstance().BeginTransaction();
				SqlCommand cmd = new SqlCommand();

				cmd.Connection = user.Connection.getInstance();
				cmd.Transaction = trans;
				cmd.CommandType = CommandType.StoredProcedure;
				if (!deep) FileManger.loadFromFolder(cmd, this, folderName, mViewer);
				else FileManger.deepLoadFromFolder(cmd, this, folderName, mViewer);
				trans.Commit();
				mViewer.Invoke(
					new StopViewerDelegate(mViewer.stop),
					new object[] {""}
				);
			} 
			catch (SqlException e) 
			{	
				if (trans != null) trans.Rollback();
				mViewer.Invoke(
					new StopViewerDelegate(mViewer.stop),
					new object[] {e.Message}
				);
			} 
			catch (Exception e) 
			{
				if (trans != null) trans.Rollback();
				mViewer.Invoke(
					new StopViewerDelegate(mViewer.stop),
					new object[] {e.Message}
				);
			} 
		}

		/// <summary>
		/// Dosen't wrap the exceptions(i.e. danegrouse code). 
		/// To be used only in this class.
		/// </summary>
		/// <param name="id">the identifier of the current category</param>
		/// <param name="cmd">the <code>SqlComand</code> to be used</param>
		protected void load(Int32 id, SqlCommand cmd) 
		{
			cmd.CommandText = "sp_GetCategory";
			cmd.CommandType = CommandType.StoredProcedure;
			cmd.Parameters.Clear();
			cmd.Parameters.Add("@Category_Id", id);

			SqlDataReader reader = cmd.ExecuteReader();
			if (reader.Read())
			{
				categoryName = (string)reader["category_name"];
				dateModified = (DateTime)reader["date_modified"];
				shared = (bool)reader["shared"];
				owner = (string)reader["owner"];
			}
			reader.Close();
			this.id = id;
		}

		private IList getSubCategoryIds(SqlCommand cmd) 
		{
			cmd.CommandText = "sp_GetAllSubcategoryIds";
			cmd.CommandType = CommandType.StoredProcedure;
			cmd.Parameters.Clear();
			cmd.Parameters.Add("@Category_Id", id);
				
			SqlDataReader categoryReader = cmd.ExecuteReader();
			IList categoryIds = new ArrayList(5);
			while (categoryReader.Read())
			{
				Int32 categoryId = (Int32)categoryReader["id"];
				categoryIds.Add(categoryId);
			}
			categoryReader.Close();
			return categoryIds;
		}

		private IList getSubImageIds(SqlCommand cmd) 
		{
			cmd.CommandText = "sp_GetImageIdsInCategory";
			cmd.CommandType = CommandType.StoredProcedure;
			cmd.Parameters.Clear();
			cmd.Parameters.Add("@Category_Id", id);
				
			SqlDataReader imageReader = cmd.ExecuteReader();
			IList imageIds = new ArrayList(5);
			while (imageReader.Read())
			{
				Int32 imageId = (Int32)imageReader["image_id"];
				imageIds.Add(imageId);
			}
			imageReader.Close();
			return imageIds;
		}

	}
}
