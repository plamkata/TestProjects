using System;
using System.Drawing;
using System.Collections;
using System.ComponentModel;
using System.Windows.Forms;
using System.Threading;

namespace PhotoAlbumGUI.Dialogs
{
	/// <summary>
	/// Summary description for LoadingDialog.
	/// </summary>
	public class LoadingDialog : 
		System.Windows.Forms.Form, PhotoAlbumDB.Entities.LoadInfoViewer
	{
		private System.Windows.Forms.Panel ContentsPanel;
		private System.Windows.Forms.Panel ButtonPanel;
		private System.Windows.Forms.ProgressBar ProgressBar;
		private System.Windows.Forms.Label Label1;
		private System.Windows.Forms.Label LoadingLabel;
		private System.Windows.Forms.Button ButtonCancel;
		/// <summary>
		/// Required designer variable.
		/// </summary>
		private System.ComponentModel.Container components = null;
		private Thread mFileLoaderThread = null;

		public LoadingDialog(Thread loaderThread)
		{
			InitializeComponent();
			mFileLoaderThread = loaderThread;
		}

		public void showFileName(String fileName) 
		{
			LoadingLabel.Text = fileName;
		}

		public void showProccess(int percent) 
		{
			ProgressBar.Value = percent;
			ProgressBar.Update();
		}

		public void stop(string errorMsg) 
		{
			mFileLoaderThread.Abort();
			if (!errorMsg.Equals(""))
			{
				MessageBox.Show(this, errorMsg, "Info", 
					MessageBoxButtons.OK, MessageBoxIcon.Information);
			}
			Dispose();
		}

		internal void startLoading() 
		{
			mFileLoaderThread.Start();
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
			this.ContentsPanel = new System.Windows.Forms.Panel();
			this.LoadingLabel = new System.Windows.Forms.Label();
			this.Label1 = new System.Windows.Forms.Label();
			this.ProgressBar = new System.Windows.Forms.ProgressBar();
			this.ButtonPanel = new System.Windows.Forms.Panel();
			this.ButtonCancel = new System.Windows.Forms.Button();
			this.ContentsPanel.SuspendLayout();
			this.ButtonPanel.SuspendLayout();
			this.SuspendLayout();
			// 
			// ContentsPanel
			// 
			this.ContentsPanel.Controls.Add(this.LoadingLabel);
			this.ContentsPanel.Controls.Add(this.Label1);
			this.ContentsPanel.Controls.Add(this.ProgressBar);
			this.ContentsPanel.Dock = System.Windows.Forms.DockStyle.Fill;
			this.ContentsPanel.Location = new System.Drawing.Point(0, 0);
			this.ContentsPanel.Name = "ContentsPanel";
			this.ContentsPanel.Size = new System.Drawing.Size(346, 64);
			this.ContentsPanel.TabIndex = 0;
			// 
			// LoadingLabel
			// 
			this.LoadingLabel.Location = new System.Drawing.Point(72, 40);
			this.LoadingLabel.Name = "LoadingLabel";
			this.LoadingLabel.Size = new System.Drawing.Size(256, 16);
			this.LoadingLabel.TabIndex = 2;
			// 
			// Label1
			// 
			this.Label1.Location = new System.Drawing.Point(16, 40);
			this.Label1.Name = "Label1";
			this.Label1.Size = new System.Drawing.Size(48, 16);
			this.Label1.TabIndex = 1;
			this.Label1.Text = "Loading:";
			// 
			// ProgressBar
			// 
			this.ProgressBar.Location = new System.Drawing.Point(16, 16);
			this.ProgressBar.Name = "ProgressBar";
			this.ProgressBar.Size = new System.Drawing.Size(312, 16);
			this.ProgressBar.Step = 1;
			this.ProgressBar.TabIndex = 0;
			// 
			// ButtonPanel
			// 
			this.ButtonPanel.Controls.Add(this.ButtonCancel);
			this.ButtonPanel.Dock = System.Windows.Forms.DockStyle.Bottom;
			this.ButtonPanel.Location = new System.Drawing.Point(0, 64);
			this.ButtonPanel.Name = "ButtonPanel";
			this.ButtonPanel.Size = new System.Drawing.Size(346, 32);
			this.ButtonPanel.TabIndex = 1;
			// 
			// ButtonCancel
			// 
			this.ButtonCancel.DialogResult = System.Windows.Forms.DialogResult.Cancel;
			this.ButtonCancel.Location = new System.Drawing.Point(128, 0);
			this.ButtonCancel.Name = "ButtonCancel";
			this.ButtonCancel.Size = new System.Drawing.Size(75, 24);
			this.ButtonCancel.TabIndex = 0;
			this.ButtonCancel.Text = "Cancel";
			this.ButtonCancel.Click += new System.EventHandler(this.ButtonCancel_Click);
			// 
			// LoadingDialog
			// 
			this.AutoScaleBaseSize = new System.Drawing.Size(5, 13);
			this.ClientSize = new System.Drawing.Size(346, 96);
			this.ControlBox = false;
			this.Controls.Add(this.ContentsPanel);
			this.Controls.Add(this.ButtonPanel);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
			this.Name = "LoadingDialog";
			this.Text = "Loading...";
			this.ContentsPanel.ResumeLayout(false);
			this.ButtonPanel.ResumeLayout(false);
			this.ResumeLayout(false);

		}
		#endregion

		private void ButtonCancel_Click(object sender, System.EventArgs e)
		{
			mFileLoaderThread.Abort();
			Dispose();
		}
	}
}
