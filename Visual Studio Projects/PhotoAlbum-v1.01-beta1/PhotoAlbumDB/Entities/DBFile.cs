using System;

namespace PhotoAlbumDB.Entities
{
	/// <summary>
	/// This interface is an abstraction of a DB item.
	/// Either a Category, or an Image. It specifies 
	/// common properties of both.
	/// </summary>
	public interface DBFile : DBObject
	{
		/// <summary>
		/// The name of the <code>DBFile</code>.
		/// </summary>
		String Name
		{
			get;
			set;
		}
		
		/// <summary>
		/// The size of the <code>DBFile</code>.
		/// If this is a <code>Category</code> the 
		/// size is calculated by the stored procedure
		/// <code>[dbo].[sp_GetSizeOfCategory]</code>
		/// </summary>
		Int64 Size
		{
			get;
		}

		/// <summary>
		/// The date the <code>DBFile</code> was modified.
		/// </summary>
		DateTime DateModified 
		{
			get;
		}
		
		/// <summary>
		/// Identifies if this <code>DBFile</code> is shared
		/// and also shares it by setting this property.
		/// </summary>
		bool Shared 
		{
			get;
			set;
		}

		/// <summary>
		/// The user who owns this picture.
		/// </summary>
		String Owner 
		{
			get;
		}

		/// <summary>
		/// Shows the DBFile to the user (i.e. displays the image or images).
		/// To be called from the Director of the Builder Pattern;
		/// </summary>
		void view(Image.ImageViewer imageViewer, bool isThumbnail);

	}
}
