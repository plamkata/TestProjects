using System;
using System.Drawing;
using System.Collections;
using System.ComponentModel;
using System.Windows.Forms;

namespace PhotoAlbumGUI.Dialogs
{
	/// <summary>
	/// Summary description for ChangeUserInfoDialog.
	/// </summary>
	public class ChangeProfileDialog : System.Windows.Forms.Form
	{
		private System.Windows.Forms.Panel ContentsPanel;
		private System.Windows.Forms.Panel ButtonPanel;
		private System.Windows.Forms.Label LabelAlias;
		private System.Windows.Forms.TextBox TextBoxAlias;
		private System.Windows.Forms.Label LabelPassword;
		private System.Windows.Forms.TextBox TextBoxPassword;
		private System.Windows.Forms.Label LabelName;
		private System.Windows.Forms.TextBox TextBoxUserName;
		private System.Windows.Forms.Label LabelUserSurname;
		private System.Windows.Forms.TextBox TextBoxUserSurname;
		private System.Windows.Forms.Button ButtonOK;
		private System.Windows.Forms.Button ButtonCancel;
		private System.Windows.Forms.ErrorProvider ErrorProvider;
		/// <summary>
		/// Required designer variable.
		/// </summary>
		private System.ComponentModel.Container components = null;

		internal string Alias 
		{
			get 
			{
				return TextBoxAlias.Text;
			}
			set 
			{
				TextBoxAlias.Text = value;
			}
		}

		internal string UserName 
		{
			get 
			{
				return TextBoxUserName.Text;
			}
			set 
			{
				TextBoxUserName.Text = value;
			}
		}

		internal string UserSurname 
		{
			get 
			{
				return TextBoxUserSurname.Text;
			}
			set 
			{
				TextBoxUserSurname.Text = value;
			}
		}

		internal string Password 
		{
			get 
			{
				return TextBoxPassword.Text;
			}
		}

		public ChangeProfileDialog()
		{
			//
			// Required for Windows Form Designer support
			//
			InitializeComponent();

			//
			// TODO: Add any constructor code after InitializeComponent call
			//
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
			this.TextBoxUserSurname = new System.Windows.Forms.TextBox();
			this.LabelUserSurname = new System.Windows.Forms.Label();
			this.TextBoxUserName = new System.Windows.Forms.TextBox();
			this.LabelName = new System.Windows.Forms.Label();
			this.TextBoxPassword = new System.Windows.Forms.TextBox();
			this.LabelPassword = new System.Windows.Forms.Label();
			this.TextBoxAlias = new System.Windows.Forms.TextBox();
			this.LabelAlias = new System.Windows.Forms.Label();
			this.ButtonPanel = new System.Windows.Forms.Panel();
			this.ButtonCancel = new System.Windows.Forms.Button();
			this.ButtonOK = new System.Windows.Forms.Button();
			this.ErrorProvider = new System.Windows.Forms.ErrorProvider();
			this.ContentsPanel.SuspendLayout();
			this.ButtonPanel.SuspendLayout();
			this.SuspendLayout();
			// 
			// ContentsPanel
			// 
			this.ContentsPanel.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
				| System.Windows.Forms.AnchorStyles.Left) 
				| System.Windows.Forms.AnchorStyles.Right)));
			this.ContentsPanel.Controls.Add(this.TextBoxUserSurname);
			this.ContentsPanel.Controls.Add(this.LabelUserSurname);
			this.ContentsPanel.Controls.Add(this.TextBoxUserName);
			this.ContentsPanel.Controls.Add(this.LabelName);
			this.ContentsPanel.Controls.Add(this.TextBoxPassword);
			this.ContentsPanel.Controls.Add(this.LabelPassword);
			this.ContentsPanel.Controls.Add(this.TextBoxAlias);
			this.ContentsPanel.Controls.Add(this.LabelAlias);
			this.ContentsPanel.Location = new System.Drawing.Point(0, 0);
			this.ContentsPanel.Name = "ContentsPanel";
			this.ContentsPanel.Size = new System.Drawing.Size(248, 160);
			this.ContentsPanel.TabIndex = 0;
			// 
			// TextBoxUserSurname
			// 
			this.TextBoxUserSurname.Location = new System.Drawing.Point(96, 120);
			this.TextBoxUserSurname.MaxLength = 50;
			this.TextBoxUserSurname.Name = "TextBoxUserSurname";
			this.TextBoxUserSurname.TabIndex = 7;
			this.TextBoxUserSurname.Text = "";
			// 
			// LabelUserSurname
			// 
			this.LabelUserSurname.Location = new System.Drawing.Point(16, 120);
			this.LabelUserSurname.Name = "LabelUserSurname";
			this.LabelUserSurname.Size = new System.Drawing.Size(64, 16);
			this.LabelUserSurname.TabIndex = 6;
			this.LabelUserSurname.Text = "Surname:";
			// 
			// TextBoxUserName
			// 
			this.TextBoxUserName.Location = new System.Drawing.Point(96, 88);
			this.TextBoxUserName.MaxLength = 50;
			this.TextBoxUserName.Name = "TextBoxUserName";
			this.TextBoxUserName.TabIndex = 5;
			this.TextBoxUserName.Text = "";
			// 
			// LabelName
			// 
			this.LabelName.Location = new System.Drawing.Point(16, 88);
			this.LabelName.Name = "LabelName";
			this.LabelName.Size = new System.Drawing.Size(64, 16);
			this.LabelName.TabIndex = 4;
			this.LabelName.Text = "Name:";
			// 
			// TextBoxPassword
			// 
			this.TextBoxPassword.Location = new System.Drawing.Point(96, 56);
			this.TextBoxPassword.MaxLength = 50;
			this.TextBoxPassword.Name = "TextBoxPassword";
			this.TextBoxPassword.PasswordChar = '*';
			this.TextBoxPassword.TabIndex = 3;
			this.TextBoxPassword.Text = "";
			this.TextBoxPassword.Validating += new System.ComponentModel.CancelEventHandler(this.TextBoxPassword_Validating);
			// 
			// LabelPassword
			// 
			this.LabelPassword.Location = new System.Drawing.Point(16, 56);
			this.LabelPassword.Name = "LabelPassword";
			this.LabelPassword.Size = new System.Drawing.Size(64, 16);
			this.LabelPassword.TabIndex = 2;
			this.LabelPassword.Text = "Password:";
			// 
			// TextBoxAlias
			// 
			this.TextBoxAlias.Enabled = false;
			this.TextBoxAlias.Location = new System.Drawing.Point(96, 24);
			this.TextBoxAlias.MaxLength = 50;
			this.TextBoxAlias.Name = "TextBoxAlias";
			this.TextBoxAlias.TabIndex = 1;
			this.TextBoxAlias.Text = "";
			// 
			// LabelAlias
			// 
			this.LabelAlias.Location = new System.Drawing.Point(16, 24);
			this.LabelAlias.Name = "LabelAlias";
			this.LabelAlias.Size = new System.Drawing.Size(64, 16);
			this.LabelAlias.TabIndex = 0;
			this.LabelAlias.Text = "Alias:";
			// 
			// ButtonPanel
			// 
			this.ButtonPanel.Controls.Add(this.ButtonCancel);
			this.ButtonPanel.Controls.Add(this.ButtonOK);
			this.ButtonPanel.Dock = System.Windows.Forms.DockStyle.Bottom;
			this.ButtonPanel.Location = new System.Drawing.Point(0, 160);
			this.ButtonPanel.Name = "ButtonPanel";
			this.ButtonPanel.Size = new System.Drawing.Size(242, 48);
			this.ButtonPanel.TabIndex = 1;
			// 
			// ButtonCancel
			// 
			this.ButtonCancel.DialogResult = System.Windows.Forms.DialogResult.Cancel;
			this.ButtonCancel.Location = new System.Drawing.Point(136, 8);
			this.ButtonCancel.Name = "ButtonCancel";
			this.ButtonCancel.TabIndex = 1;
			this.ButtonCancel.Text = "Cancel";
			// 
			// ButtonOK
			// 
			this.ButtonOK.DialogResult = System.Windows.Forms.DialogResult.OK;
			this.ButtonOK.Location = new System.Drawing.Point(32, 8);
			this.ButtonOK.Name = "ButtonOK";
			this.ButtonOK.TabIndex = 0;
			this.ButtonOK.Text = "OK";
			// 
			// ErrorProvider
			// 
			this.ErrorProvider.BlinkStyle = System.Windows.Forms.ErrorBlinkStyle.NeverBlink;
			this.ErrorProvider.ContainerControl = this;
			// 
			// ChangeProfileDialog
			// 
			this.AutoScaleBaseSize = new System.Drawing.Size(5, 13);
			this.ClientSize = new System.Drawing.Size(242, 208);
			this.Controls.Add(this.ButtonPanel);
			this.Controls.Add(this.ContentsPanel);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
			this.Name = "ChangeProfileDialog";
			this.Text = "Change Profile Dialog";
			this.ContentsPanel.ResumeLayout(false);
			this.ButtonPanel.ResumeLayout(false);
			this.ResumeLayout(false);

		}
		#endregion

		private void TextBoxPassword_Validating(object sender, 
			System.ComponentModel.CancelEventArgs e)
		{
			if (TextBoxPassword.Text == null || TextBoxPassword.Text.Length < 3)
			{
				ErrorProvider.SetError(TextBoxPassword, 
					@"Password should have at least 3 characters!");
				e.Cancel = true;
			}
			else
			{
				ErrorProvider.SetError(TextBoxPassword, "");
			}
		}
	}
}
