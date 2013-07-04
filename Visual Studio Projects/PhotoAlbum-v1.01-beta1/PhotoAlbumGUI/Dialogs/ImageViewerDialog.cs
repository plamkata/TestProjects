using System;
using System.Drawing;
using System.Collections;
using System.ComponentModel;
using System.Windows.Forms;

namespace PhotoAlbumGUI.Dialogs
{
	/// <summary>
	/// Summary description for ImageViewerDialog.
	/// </summary>
	public class ImageViewerDialog : System.Windows.Forms.Form
	{
		private System.Windows.Forms.PictureBox ViewerPictureBox;
		/// <summary>
		/// Required designer variable.
		/// </summary>
		private System.ComponentModel.Container components = null;

		internal System.Drawing.Image Image 
		{
			set 
			{
				ViewerPictureBox.Image = value;
			}
		}

		public ImageViewerDialog()
		{
			InitializeComponent();
		}

		/// <summary>
		/// Clean up any resources being used.
		/// </summary>
		protected override void Dispose( bool disposing )
		{
			if( disposing )
			{
				if(components != null)
				{
					components.Dispose();
				}
				if (ViewerPictureBox != null) 
				{
					ViewerPictureBox.Dispose();
				}
			}
			base.Dispose( disposing );
		}

		#region Windows Form Designer generated code
		/// <summary>
		/// Required method for Designer support - do not modify
		/// the contents of this method with the code editor.
		/// </summary>
		private void InitializeComponent()
		{
			this.ViewerPictureBox = new System.Windows.Forms.PictureBox();
			this.SuspendLayout();
			// 
			// ViewerPictureBox
			// 
			this.ViewerPictureBox.Dock = System.Windows.Forms.DockStyle.Fill;
			this.ViewerPictureBox.Location = new System.Drawing.Point(0, 0);
			this.ViewerPictureBox.Name = "ViewerPictureBox";
			this.ViewerPictureBox.Size = new System.Drawing.Size(576, 470);
			this.ViewerPictureBox.SizeMode = System.Windows.Forms.PictureBoxSizeMode.StretchImage;
			this.ViewerPictureBox.TabIndex = 0;
			this.ViewerPictureBox.TabStop = false;
			// 
			// ImageViewerDialog
			// 
			this.AutoScaleBaseSize = new System.Drawing.Size(5, 13);
			this.AutoScroll = true;
			this.BackColor = System.Drawing.SystemColors.ControlLightLight;
			this.ClientSize = new System.Drawing.Size(576, 470);
			this.Controls.Add(this.ViewerPictureBox);
			this.Name = "ImageViewerDialog";
			this.Text = "Image Viewer Dialog";
			this.ResumeLayout(false);

		}
		#endregion
	}
}
