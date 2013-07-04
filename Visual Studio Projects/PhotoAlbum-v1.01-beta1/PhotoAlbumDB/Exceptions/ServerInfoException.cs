using System;
using System.Data.SqlClient;

namespace PhotoAlbumDB.Exceptions
{
	/// <summary>
	/// Contains the information raised in the stored proc.
	/// </summary>
	public class ServerInfoException : ApplicationException
	{
		private SqlErrorCollection infos;

		public SqlErrorCollection Infos 
		{
			get 
			{
				return infos;
			}
		}
		
		public ServerInfoException(String aMessage, SqlErrorCollection listInfos, 
			Exception aCauseException) : base(aMessage, aCauseException)
		{
			infos = listInfos;
		}

		public ServerInfoException(String aMessage, 
			SqlErrorCollection listInfos) : base(aMessage, null)
		{
			infos = listInfos;
		}

		public ServerInfoException(String aMessage, 
			Exception aCauseException) : this(aMessage, null, aCauseException)
		{
		}
	}
}
