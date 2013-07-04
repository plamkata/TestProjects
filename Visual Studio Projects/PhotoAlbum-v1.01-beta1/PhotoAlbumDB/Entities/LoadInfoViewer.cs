using System;

namespace PhotoAlbumDB.Entities
{

	public delegate void ShowFilenameDlegate(string aFileName);
	public delegate void ShowProccessDlegate(int percent);
	public delegate void StopViewerDelegate(string errorMsg);

	public interface LoadInfoViewer
	{
		void showFileName(String fileName);

		void showProccess(int percent);

		void stop(string errorMsg);

		object Invoke(System.Delegate method, object[] args);
	}
}
