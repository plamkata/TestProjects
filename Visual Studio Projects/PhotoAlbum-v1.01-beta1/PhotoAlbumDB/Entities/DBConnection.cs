using System;
using System.Diagnostics;
using System.Text;
using System.Data;
using System.Data.SqlClient;

namespace PhotoAlbumDB.Entities
{
	/// <summary>
	/// This class is a singleton.
	/// It stores the connection to the DB
	/// and should be used only in this project.
	/// </summary>
	internal sealed class DBConnection
	{		
		private SqlConnection conn;
		private SqlErrorCollection serverInfos;

		internal SqlErrorCollection ServerInfos
		{
			get 
			{
				return serverInfos;
			}
		}

		internal DBConnection()
		{
		}

		/// <summary>
		/// Use this method to get the current connection.
		/// </summary>
		/// <returns>The <code>SqlConnection</code> to 
		/// the server.</returns>
		internal SqlConnection getInstance() 
		{
			if (conn == null) 
			{
				String connString = (String)System.Configuration.
					ConfigurationSettings.AppSettings["conn"];
				conn = new SqlConnection(connString);
				conn.InfoMessage += new SqlInfoMessageEventHandler(OnInfoMessage);
				conn.Open();
			}
			else if (conn.State == ConnectionState.Closed) 
			{
				conn.Open();
			}
			return conn;
		}

		internal void closeConnection() 
		{
			if (conn != null) 
			{
				conn.Close();
			}
		}

		private void OnInfoMessage(object sender, SqlInfoMessageEventArgs args)
		{
			if (args.Errors.Count > 0 ) 
			{
				serverInfos = args.Errors;
				// TODO: enable asynchronouse output to the gui
//				StringBuilder builder = new StringBuilder();
//				foreach(SqlError error in args.Errors) 
//				{
//					builder.Remove(0, builder.Length - 1);
//					builder.Append("***DBInfoMessage: severity = " + error.Number);
//					builder.Append(", state = " + error.State + "\n");
//					builder.Append("***\tmessage = " + error.Message);
//					builder.Append(", procedure = " + error.Procedure);
//					builder.Append(", line = " + error.LineNumber);
//					Debug.Write(builder.ToString());
//				}
			}
		}
	}
}