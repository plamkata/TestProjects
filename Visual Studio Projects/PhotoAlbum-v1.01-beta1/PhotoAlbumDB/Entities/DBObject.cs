using System;

namespace PhotoAlbumDB.Entities
{
	/// <summary>
	/// An abstraction of a database object.
	/// </summary>
	public interface DBObject
	{
		/// <summary>
		/// Creates a new row in the database table, which
		/// is represented by an instance of the class 
		/// implementing this interface.
		/// </summary>
		void save();

		/// <summary>
		/// Deletes the row of the database table
		/// specified by an instance of this type.
		/// </summary>
		void delete();

		/// <summary>
		/// Applies the changes on an instance
		/// of this type to the database.
		/// </summary>
		void update();
	}
}
