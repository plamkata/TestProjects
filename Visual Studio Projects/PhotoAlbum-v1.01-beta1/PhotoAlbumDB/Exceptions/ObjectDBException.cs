using System;
using System.Data;
using System.Data.SqlClient;
using System.Data.SqlTypes;
using PhotoAlbumDB.Entities;

namespace PhotoAlbumDB.Exceptions
{
	/// <summary>
	/// Occures if the comunication of an instance of type ObjectDB	
	/// with the database server throws some exception
	/// </summary>
	public class ObjectDBException : ApplicationException
	{
		private DBObject source;
		
		public DBObject ObjectSource 
		{
			get 
			{
				return source;
			}
		}

		public ObjectDBException(String aMessage, DBObject aSource, 
			Exception aCauseException) : base(aMessage, aCauseException)
		{
			source = aSource;
		}

		public ObjectDBException(String aMessage, 
			DBObject aSource) : base(aMessage, null)
		{
			source = aSource;
		}

		public ObjectDBException(String aMessage, 
			Exception aCauseException) : this(aMessage, null, aCauseException)
		{
		}
	}
}
