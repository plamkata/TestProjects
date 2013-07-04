using System;
using System.Data;
using System.Data.SqlClient;
using System.Collections;
using PhotoAlbumDB.Exceptions;

namespace PhotoAlbumDB.Entities
{
	/// <summary>
	/// This is the general object representing our 
	/// current user.
	/// </summary>
	public class User : DBObject, IDisposable
	{
		private Int32 id = -1;
		private string alias;
		private string password;
		private string userName;
		private string userSurname;
		private Category rootCategory;

		private DBConnection mDBConnection = null;
		private bool disposed = false;

		/// <summary>
		/// Cannot change alias.
		/// Should call save() after initializing
		/// this property when creating a new User object. 
		/// </summary>
		public string Alias 
		{
			get 
			{
				return alias;
			}
			set 
			{
				if (alias != null) 
					throw new ObjectDBException("Cannot change user alias!", this);
				this.alias = value;
			}
		}

		/// <summary>
		/// Should call update() after changing the name 
		/// in order to take effect in DB
		/// </summary>
		public string Name 
		{
			get 
			{
				return userName;
			}
			set 
			{
				this.userName = value;
			}

		}

		/// <summary>
		/// Should call update() after changing the Surname 
		/// in order to take effect in DB
		/// </summary>
		public string Surname 
		{
			get 
			{
				return userSurname;
			}
			set 
			{
				this.userSurname = value;
			}
		}

		/// <summary>
		/// Setting the Password property is heavy operation.
		/// It dosen't require update(); dose its work alone. 
		/// </summary>
		public string Password 
		{
			set
			{
				if (password == null && id == -1) 
				{ 
					password = value;
				} 
				else 
				{ // change password
					try 
					{
						SqlCommand cmd = new SqlCommand(
							"sp_ChangeUserPassword", 
							mDBConnection.getInstance()
						);
						cmd.CommandType = CommandType.StoredProcedure;
						cmd.Parameters.Add("@Alias", alias);
						cmd.Parameters.Add("@Password", password);
						cmd.Parameters.Add("@New_Password", value);
						SqlParameter userIdParam = 
							cmd.Parameters.Add("@User_Id", SqlDbType.Int);
						userIdParam.Direction = ParameterDirection.Output;

						cmd.ExecuteNonQuery();
					
						if (id != (Int32) userIdParam.Value) 
							throw new ObjectDBException(
								"Problem changing user passsword", this); 
						password = value;
					} 
					catch (SqlException e) 
					{	
						throw new ServerInfoException(
							e.Message, mDBConnection.ServerInfos, e);	
					} 
					catch (Exception e) 
					{
						if (e is ServerInfoException) throw e;
						else 
						{
							throw new ObjectDBException(
								"Problem updating user!", this, e);
						}
					}
				}
			}
		}

		/// <summary>
		/// The outomaticaly generated root category of the user
		/// </summary>
		public Category RootCategory 
		{
			get 
			{
				return rootCategory;
			}
		}
		internal Int32 ID 
		{
			get 
			{
				return id;
			}
		}

		internal DBConnection Connection 
		{
			get 
			{
				return mDBConnection;
			}
		}
		public User()
		{	
			mDBConnection = new DBConnection();
		}
		

		public void Dispose()
		{
			Dispose(true);
			GC.SuppressFinalize(this);
		}

		protected virtual void Dispose(bool disposing)
		{
			// Check to see if Dispose has already been called.
			if(!this.disposed)
			{
				if(disposing)
				{
					try 
					{
						performLogout();
					} 
					catch (Exception) {}
					mDBConnection.closeConnection();
					this.alias = null;
					this.rootCategory = null;
					// TODO: Add rootCategory.Dispose();
				}
			}
			disposed = true;         
		}

		#region DBObject Members

		public void save()
		{
			if (this.id >= 0) 
			{
				// User should not have an id to be inserted
				throw new ObjectDBException("Problem creating user!", this);
			}
			try 
			{
				SqlCommand cmd = new SqlCommand(
					"sp_CreateNewUser", 
					mDBConnection.getInstance()
				);
				cmd.CommandType = CommandType.StoredProcedure;
				cmd.Parameters.Add("@Alias", alias);
				cmd.Parameters.Add("@Password", password);
				cmd.Parameters.Add("@Name", userName);
				cmd.Parameters.Add("@Surname", userSurname);
				SqlParameter userIdParam = 
					cmd.Parameters.Add("@User_Id", SqlDbType.Int);
				userIdParam.Direction = ParameterDirection.Output;
				SqlParameter rootCategoryIdParam = 
					cmd.Parameters.Add("@Root_Category_Id", SqlDbType.Int);
				rootCategoryIdParam.Direction = ParameterDirection.Output;
				
				cmd.ExecuteNonQuery();
				
				id = (Int32) userIdParam.Value;
				Int32 rootCategoryId = (Int32) rootCategoryIdParam.Value;
				rootCategory = new Category(rootCategoryId, cmd, this);
			} 
			catch (SqlException e) 
			{	
				throw new ServerInfoException(
					e.Message, mDBConnection.ServerInfos, e);	
			} 
			catch (Exception e) 
			{
				if (e is ServerInfoException) throw e;
				else 
				{
					throw new ObjectDBException("Problem loading user", this, e);
				}

			}
		}

		public void delete()
		{
			if (this.id == -1) 
			{ 
				// only existing users can be deleted
				throw new ObjectDBException("Problem deleting user!", this);
			}
			try 
			{
				SqlCommand cmd = new SqlCommand(
					"sp_DeleteUserById", 
					mDBConnection.getInstance()
				);
				cmd.CommandType = CommandType.StoredProcedure;
				cmd.Parameters.Add("@User_Id", id);
				
				cmd.ExecuteNonQuery();
				// TODO: point Category to null i.e cascade delete DBObjects
				id = -1;
				alias = null;
				password = null;
				userSurname = null;
				userName = null;
				rootCategory = null;
			} 
			catch (SqlException e) 
			{	
				throw new ServerInfoException(
					e.Message, mDBConnection.ServerInfos, e);	
			} 
			catch (Exception e) 
			{
				if (e is ServerInfoException) throw e;
				else 
				{
					throw new ObjectDBException("Problem loading user", this, e);
				}

			}
			Dispose();
		}

		public void update()
		{
			if (this.id == -1) 
			{ 
				// only existing users can be updated
				throw new ObjectDBException("Problem updating user!", this);
			}
			try 
			{
				SqlCommand cmd = new SqlCommand(
					"sp_ChangeUserInfo", 
					mDBConnection.getInstance()
				);
				cmd.CommandType = CommandType.StoredProcedure;
				cmd.Parameters.Add("@Alias", alias);
				cmd.Parameters.Add("@Password", password);
				cmd.Parameters.Add("@New_Password", password);
				cmd.Parameters.Add("@Name", userName);
				cmd.Parameters.Add("@Surname", userSurname);
				
				cmd.ExecuteNonQuery();
			} 
			catch (SqlException e) 
			{	
				throw new ServerInfoException(
					e.Message, mDBConnection.ServerInfos, e);	
			} 
			catch (Exception e) 
			{
				if (e is ServerInfoException) throw e;
				else 
				{
					throw new ObjectDBException("Problem loading user", this, e);
				}
			}
		}

		#endregion

		public void performLogin(String loginame, String pass) 
		{
			try 
			{
				SqlCommand cmd = new SqlCommand(
					"sp_PerformLogin", 
					mDBConnection.getInstance()
				);
				cmd.CommandType = CommandType.StoredProcedure;

				cmd.Parameters.Add("@Loginame", loginame);
				cmd.Parameters.Add("@Password", pass);
				SqlParameter userIdParam = 
					cmd.Parameters.Add("@User_Id", SqlDbType.Int);
				userIdParam.Direction = ParameterDirection.Output;
				cmd.ExecuteNonQuery();
				
				Int32 userId = (Int32) userIdParam.Value;
				load(userId, cmd);
				password = pass;
			} 
			catch (SqlException e) 
			{
				string msg = e.Message;
				if (msg.IndexOf("\n") > 0) 
				{
					msg = msg.Substring(0, msg.IndexOf("\r\n"));
				}
				throw new ServerInfoException(msg, mDBConnection.ServerInfos, e);	
			} 
			catch (Exception e) 
			{
				if (e is ServerInfoException) throw e;
				else 
				{
					throw new ObjectDBException("Problem loading user", this, e);
				}
			}
		}

		public void performLogout() 
		{
			// end of session with user
			mDBConnection.closeConnection();
			rootCategory = null;
		}

		public bool checkUser(String alias, String password) 
		{
			if (alias.Equals(this.alias) && 
				password.Equals(this.password)) 
			{
				return true;
			} 
			else 
			{
				throw new ServerInfoException("Password incorrect!", 
					mDBConnection.ServerInfos);
			}
		}

		public ArrayList viewSharedDBItems() 
		{
			ArrayList dbItems = new ArrayList(20);
			SqlTransaction trans = null;
			try 
			{
				trans = mDBConnection.getInstance().
					BeginTransaction(IsolationLevel.Serializable);
				SqlCommand cmd = new SqlCommand();
				cmd.Connection = mDBConnection.getInstance();
				cmd.Transaction = trans;
				loadSharedCategories(cmd, dbItems);
				loadSharedImages(cmd, dbItems);
				trans.Commit();
			} 
			catch (SqlException e) 
			{	
				if (trans != null) trans.Rollback();
				throw new ServerInfoException(e.Message, 
					mDBConnection.ServerInfos, e);	
			} 
			catch (Exception e) 
			{
				if (trans != null) trans.Rollback();
				if (e is ServerInfoException) throw e;
				else 
				{
					throw new ObjectDBException("Problem loading shared items!", this, e);
				}
			} 
			return dbItems;
		}

		/// <summary>
		/// Uses the same conn object (dosen't close the conn)
		/// and dosen't wrap the exceptions. 
		/// To be used only in this class.
		/// </summary>
		/// <param name="id">the identifier of the current user</param>
		protected void load(Int32 id, SqlCommand cmd) 
		{
			cmd.CommandText = "sp_GetUser";
			cmd.Parameters.Clear();
			cmd.Parameters.Add("@User_Id", id);

			SqlDataReader reader = cmd.ExecuteReader();
			Int32 rootCategoryId = -1;
			if (reader.Read())
			{
				alias = (string)reader["alias"];
				userName = (string)reader["user_name"];
				userSurname = (string)reader["user_surname"];
				rootCategoryId = (Int32)reader["root_category_id"];
			}
			reader.Close();
			
			if (rootCategoryId != -1) 
			{
				rootCategory = new Category(rootCategoryId, cmd, this);
			}
			this.id = id;
		}


		private void loadSharedCategories(SqlCommand cmd, ArrayList dbItems) 
		{
			cmd.CommandText = "SELECT * FROM v_RootSharedCategories"; 
			cmd.CommandType = CommandType.Text;
			cmd.Parameters.Clear();

			SqlDataReader reader = cmd.ExecuteReader();
			ArrayList catIds = new ArrayList();
			while (reader.Read()) 
			{
				Int32 catId = (Int32)reader["category_id"];
				catIds.Add(catId);
			}
			reader.Close();

			foreach (Int32 catId in catIds)
			{
				Category cat = new Category(catId, cmd, this);
				dbItems.Add(cat);
			}
		}

		private void loadSharedImages(SqlCommand cmd, ArrayList dbItems) 
		{
			cmd.CommandText = "SELECT * FROM v_SinglySharedImages"; 
			cmd.CommandType = CommandType.Text;
			cmd.Parameters.Clear();

			SqlDataReader reader = cmd.ExecuteReader();
			ArrayList imgIds = new ArrayList();
			while (reader.Read()) 
			{
				Int32 imgId = (Int32)reader["image_id"];
				imgIds.Add(imgId);
			}
			reader.Close();

			foreach (Int32 imgId in imgIds)
			{
				Image img = new Image(imgId, cmd, this);
				dbItems.Add(img);
			}
		}
	}
}
