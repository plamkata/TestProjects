using System;
using System.Drawing;
using System.Collections;
using System.ComponentModel;
using System.Windows.Forms;

namespace PhotoAlbumGUI.Dialogs
{
	/// <summary>
	/// Summary description for ChangePasswordDialog.
	/// </summary>
	public class ChangePasswordDialog : System.Windows.Forms.Form
	{
		private System.Windows.Forms.Panel ContentsPanel;
		private System.Windows.Forms.Panel ButtonPanel;
		private System.Windows.Forms.Label LabelAlias;
		private System.Windows.Forms.TextBox TextBoxAlias;
		private System.Windows.Forms.Label LabelPassword;
		private System.Windows.Forms.TextBox TextBoxPassword;
		private System.Windows.Forms.Label LabelNewPassword;
		private System.Windows.Forms.TextBox TextBoxNewPassword;
		private System.Windows.Forms.Label LabelNewPasswordAgain;
		private System.Windows.Forms.TextBox TextBoxNewPasswordAgain;
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
		internal string Password 
		{
			get 
			{
				return TextBoxPassword.Text;
			}
		}

		internal string NewPassword 
		{
			get 
			{
				return TextBoxNewPassword.Text;
			}
		}

		public ChangePasswordDialog()
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
			this.ButtonPanel = new System.Windows.Forms.Panel();
			this.LabelAlias = new System.Windows.Forms.Label();
			this.TextBoxAlias = new System.Windows.Forms.TextBox();
			this.LabelPassword = new System.Windows.Forms.Label();
			this.TextBoxPassword = new System.Windows.Forms.TextBox();
			this.LabelNewPassword = new System.Windows.Forms.Label();
			this.TextBoxNewPassword = new System.Windows.Forms.TextBox();
			this.LabelNewPasswordAgain = new System.Windows.Forms.Label();
			this.TextBoxNewPasswordAgain = new System.Windows.Forms.TextBox();
			this.ButtonOK = new System.Windows.Forms.Button();
			this.ButtonCancel = new System.Windows.Forms.Button();
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
			this.ContentsPanel.Controls.Add(this.TextBoxNewPasswordAgain);
			this.ContentsPanel.Controls.Add(this.LabelNewPasswordAgain);
			this.ContentsPanel.Controls.Add(this.TextBoxNewPassword);
			this.ContentsPanel.Controls.Add(this.LabelNewPassword);
			this.ContentsPanel.Controls.Add(this.TextBoxPassword);
			this.ContentsPanel.Controls.Add(this.LabelPassword);
			this.ContentsPanel.Controls.Add(this.TextBoxAlias);
			this.ContentsPanel.Controls.Add(this.LabelAlias);
			this.ContentsPanel.Location = new System.Drawing.Point(0, 0);
			this.ContentsPanel.Name = "ContentsPanel";
			this.ContentsPanel.Size = new System.Drawing.Size(504, 104);
			this.ContentsPanel.TabIndex = 0;
			// 
			// ButtonPanel
			// 
			this.ButtonPanel.Controls.Add(this.ButtonCancel);
			this.ButtonPanel.Controls.Add(this.ButtonOK);
			this.ButtonPanel.Dock = System.Windows.Forms.DockStyle.Bottom;
			this.ButtonPanel.Location = new System.Drawing.Point(0, 104);
			this.ButtonPanel.Name = "ButtonPanel";
			this.ButtonPanel.Size = new System.Drawing.Size(498, 40);
			this.ButtonPanel.TabIndex = 1;
			// 
			// LabelAlias
			// 
			this.LabelAlias.Location = new System.Drawing.Point(16, 24);
			this.LabelAlias.Name = "LabelAlias";
			this.LabelAlias.Size = new System.Drawing.Size(64, 16);
			this.LabelAlias.TabIndex = 0;
			this.LabelAlias.Text = "Alias:";
			// 
			// TextBoxAlias
			// 
			this.TextBoxAlias.Enabled = false;
			this.TextBoxAlias.Location = new System.Drawing.Point(88, 24);
			this.TextBoxAlias.MaxLength = 50;
			this.TextBoxAlias.Name = "TextBoxAlias";
			this.TextBoxAlias.Size = new System.Drawing.Size(112, 20);
			this.TextBoxAlias.TabIndex = 1;
			this.TextBoxAlias.TabStop = false;
			this.TextBoxAlias.Text = "";
			// 
			// LabelPassword
			// 
			this.LabelPassword.Location = new System.Drawing.Point(16, 64);
			this.LabelPassword.Name = "LabelPassword";
			this.LabelPassword.Size = new System.Drawing.Size(64, 16);
			this.LabelPassword.TabIndex = 2;
			this.LabelPassword.Text = "Password:";
			// 
			// TextBoxPassword
			// 
			this.TextBoxPassword.Location = new System.Drawing.Point(88, 64);
			this.TextBoxPassword.MaxLength = 50;
			this.TextBoxPassword.Name = "TextBoxPassword";
			this.TextBoxPassword.PasswordChar = '*';
			this.TextBoxPassword.Size = new System.Drawing.Size(112, 20);
			this.TextBoxPassword.TabIndex = 3;
			this.TextBoxPassword.Text = "";
			this.TextBoxPassword.Validating += new System.ComponentModel.CancelEventHandler(this.TextBoxPassword_Validating);
			// 
			// LabelNewPassword
			// 
			this.LabelNewPassword.Location = new System.Drawing.Point(224, 24);
			this.LabelNewPassword.Name = "LabelNewPassword";
			this.LabelNewPassword.Size = new System.Drawing.Size(100, 16);
			this.LabelNewPassword.TabIndex = 4;
			this.LabelNewPassword.Text = "New Password:";
			// 
			// TextBoxNewPassword
			// 
			this.TextBoxNewPassword.Location = new System.Drawing.Point(352, 24);
			this.TextBoxNewPassword.MaxLength = 50;
			this.TextBoxNewPassword.Name = "TextBoxNewPassword";
			this.TextBoxNewPassword.PasswordChar = '*';
			this.TextBoxNewPassword.Size = new System.Drawing.Size(120, 20);
			this.TextBoxNewPassword.TabIndex = 5;
			this.TextBoxNewPassword.Text = "";
			this.TextBoxNewPassword.Validating += new System.ComponentModel.CancelEventHandler(this.TextBoxNewPassword_Validating);
			// 
			// LabelNewPasswordAgain
			// 
			this.LabelNewPasswordAgain.Location = new System.Drawing.Point(224, 64);
			this.LabelNewPasswordAgain.Name = "LabelNewPasswordAgain";
			this.LabelNewPasswordAgain.Size = new System.Drawing.Size(120, 16);
			this.LabelNewPasswordAgain.TabIndex = 6;
			this.LabelNewPasswordAgain.Text = "New Password Again:";
			// 
			// TextBoxNewPasswordAgain
			// 
			this.TextBoxNewPasswordAgain.Location = new System.Drawing.Point(352, 64);
			this.TextBoxNewPasswordAgain.MaxLength = 50;
			this.TextBoxNewPasswordAgain.Name = "TextBoxNewPasswordAgain";
			this.TextBoxNewPasswordAgain.PasswordChar = '*';
			this.TextBoxNewPasswordAgain.Size = new System.Drawing.Size(120, 20);
			this.TextBoxNewPasswordAgain.TabIndex = 7;
			this.TextBoxNewPasswordAgain.Text = "";
			this.TextBoxNewPasswordAgain.Validating += new System.ComponentModel.CancelEventHandler(this.TextBoxNewPasswordAgain_Validating);
			// 
			// ButtonOK
			// 
			this.ButtonOK.DialogResult = System.Windows.Forms.DialogResult.OK;
			this.ButtonOK.Location = new System.Drawing.Point(128, 8);
			this.ButtonOK.Name = "ButtonOK";
			this.ButtonOK.TabIndex = 0;
			this.ButtonOK.Text = "OK";
			// 
			// ButtonCancel
			// 
			this.ButtonCancel.DialogResult = System.Windows.Forms.DialogResult.Cancel;
			this.ButtonCancel.Location = new System.Drawing.Point(248, 8);
			this.ButtonCancel.Name = "ButtonCancel";
			this.ButtonCancel.TabIndex = 1;
			this.ButtonCancel.Text = "Cancel";
			// 
			// ErrorProvider
			// 
			this.ErrorProvider.BlinkStyle = System.Windows.Forms.ErrorBlinkStyle.NeverBlink;
			this.ErrorProvider.ContainerControl = this;
			// 
			// ChangePasswordDialog
			// 
			this.AutoScaleBaseSize = new System.Drawing.Size(5, 13);
			this.ClientSize = new System.Drawing.Size(498, 144);
			this.Controls.Add(this.ButtonPanel);
			this.Controls.Add(this.ContentsPanel);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
			this.Name = "ChangePasswordDialog";
			this.Text = "Change Password Dialog";
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

		private void TextBoxNewPassword_Validating(object sender, 
			System.ComponentModel.CancelEventArgs e)
		{
			if (TextBoxNewPassword.Text == null || TextBoxNewPassword.Text.Length < 3)
			{
				ErrorProvider.SetError(TextBoxNewPassword, 
					@"Password should have at least 3 characters!");
				e.Cancel = true;
			}
			else
			{
				ErrorProvider.SetError(TextBoxNewPassword, "");
			}
		}

		private void TextBoxNewPasswordAgain_Validating(object sender, 
			System.ComponentModel.CancelEventArgs e)
		{
			if (TextBoxNewPasswordAgain.Text == null || TextBoxNewPasswordAgain.Text.Length < 3)
			{
				ErrorProvider.SetError(TextBoxNewPasswordAgain, 
					@"Password should have at least 3 characters!");
				e.Cancel = true;
			} 
			else if (!TextBoxNewPasswordAgain.Text.Equals(TextBoxNewPassword.Text)) 
			{
				ErrorProvider.SetError(TextBoxNewPasswordAgain, 
					@"Repeted password is not the same!");
				e.Cancel = true;
			}
			else
			{
				ErrorProvider.SetError(TextBoxNewPasswordAgain, "");
			}
		}
	}
}
