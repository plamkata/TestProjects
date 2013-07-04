using System;
using System.Drawing;
using System.Collections;
using System.ComponentModel;
using System.Windows.Forms;
using System.Text.RegularExpressions;

namespace PhotoAlbumGUI.Dialogs
{
	/// <summary>
	/// Summary description for NewUserDialog.
	/// </summary>
	public class NewUserDialog : System.Windows.Forms.Form
	{
		private System.Windows.Forms.Panel ContentsPanel;
		private System.Windows.Forms.Panel ButtonPanel;
		private System.Windows.Forms.Label LabelAlias;
		private System.Windows.Forms.TextBox TextBoxAlias;
		private System.Windows.Forms.Label LabelUserName;
		private System.Windows.Forms.TextBox TextBoxName;
		private System.Windows.Forms.Label LabelUserSurname;
		private System.Windows.Forms.TextBox TextBoxSurname;
		private System.Windows.Forms.Label LabelPassword;
		private System.Windows.Forms.TextBox TextBoxPassword;
		private System.Windows.Forms.Label LabelPasswordAgain;
		private System.Windows.Forms.TextBox TextBoxPasswordAgain;
		private System.Windows.Forms.Button ButtonOK;
		private System.Windows.Forms.Button ButtonCancel;
		private System.Windows.Forms.ErrorProvider ErrorProvider;
		/// <summary>
		/// Required designer variable.
		/// </summary>
		private System.ComponentModel.Container components = null;
		private Regex aliasRegex = new Regex(@"^[A-Za-z0-9_\-\.]{0,50}$");

		internal string Alias 
		{
			get 
			{
				return TextBoxAlias.Text;
			}
		}

		internal string UserName 
		{
			get 
			{
				return TextBoxName.Text;
			}
		}

		internal string UserSurname 
		{
			get 
			{
				return TextBoxSurname.Text;
			}
		}

		internal string Password 
		{
			get 
			{
				return TextBoxPassword.Text;
			}
		}

		public NewUserDialog()
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
			this.TextBoxPasswordAgain = new System.Windows.Forms.TextBox();
			this.LabelPasswordAgain = new System.Windows.Forms.Label();
			this.TextBoxPassword = new System.Windows.Forms.TextBox();
			this.LabelPassword = new System.Windows.Forms.Label();
			this.TextBoxSurname = new System.Windows.Forms.TextBox();
			this.LabelUserSurname = new System.Windows.Forms.Label();
			this.TextBoxName = new System.Windows.Forms.TextBox();
			this.LabelUserName = new System.Windows.Forms.Label();
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
			this.ContentsPanel.Controls.Add(this.TextBoxPasswordAgain);
			this.ContentsPanel.Controls.Add(this.LabelPasswordAgain);
			this.ContentsPanel.Controls.Add(this.TextBoxPassword);
			this.ContentsPanel.Controls.Add(this.LabelPassword);
			this.ContentsPanel.Controls.Add(this.TextBoxSurname);
			this.ContentsPanel.Controls.Add(this.LabelUserSurname);
			this.ContentsPanel.Controls.Add(this.TextBoxName);
			this.ContentsPanel.Controls.Add(this.LabelUserName);
			this.ContentsPanel.Controls.Add(this.TextBoxAlias);
			this.ContentsPanel.Controls.Add(this.LabelAlias);
			this.ContentsPanel.Location = new System.Drawing.Point(0, 0);
			this.ContentsPanel.Name = "ContentsPanel";
			this.ContentsPanel.Size = new System.Drawing.Size(264, 200);
			this.ContentsPanel.TabIndex = 0;
			// 
			// TextBoxPasswordAgain
			// 
			this.TextBoxPasswordAgain.Location = new System.Drawing.Point(112, 160);
			this.TextBoxPasswordAgain.MaxLength = 50;
			this.TextBoxPasswordAgain.Name = "TextBoxPasswordAgain";
			this.TextBoxPasswordAgain.PasswordChar = '*';
			this.TextBoxPasswordAgain.TabIndex = 9;
			this.TextBoxPasswordAgain.Text = "";
			this.TextBoxPasswordAgain.Validating += new System.ComponentModel.CancelEventHandler(this.TextBoxPasswordAgain_Validating);
			// 
			// LabelPasswordAgain
			// 
			this.LabelPasswordAgain.Location = new System.Drawing.Point(16, 160);
			this.LabelPasswordAgain.Name = "LabelPasswordAgain";
			this.LabelPasswordAgain.Size = new System.Drawing.Size(88, 16);
			this.LabelPasswordAgain.TabIndex = 8;
			this.LabelPasswordAgain.Text = "Password again:";
			// 
			// TextBoxPassword
			// 
			this.TextBoxPassword.Location = new System.Drawing.Point(112, 128);
			this.TextBoxPassword.MaxLength = 50;
			this.TextBoxPassword.Name = "TextBoxPassword";
			this.TextBoxPassword.PasswordChar = '*';
			this.TextBoxPassword.TabIndex = 7;
			this.TextBoxPassword.Text = "";
			this.TextBoxPassword.Validating += new System.ComponentModel.CancelEventHandler(this.TextBoxPassword_Validating);
			// 
			// LabelPassword
			// 
			this.LabelPassword.Location = new System.Drawing.Point(16, 128);
			this.LabelPassword.Name = "LabelPassword";
			this.LabelPassword.Size = new System.Drawing.Size(64, 16);
			this.LabelPassword.TabIndex = 6;
			this.LabelPassword.Text = "Password:";
			// 
			// TextBoxSurname
			// 
			this.TextBoxSurname.Location = new System.Drawing.Point(112, 96);
			this.TextBoxSurname.MaxLength = 50;
			this.TextBoxSurname.Name = "TextBoxSurname";
			this.TextBoxSurname.TabIndex = 5;
			this.TextBoxSurname.Text = "";
			// 
			// LabelUserSurname
			// 
			this.LabelUserSurname.Location = new System.Drawing.Point(16, 96);
			this.LabelUserSurname.Name = "LabelUserSurname";
			this.LabelUserSurname.Size = new System.Drawing.Size(64, 16);
			this.LabelUserSurname.TabIndex = 4;
			this.LabelUserSurname.Text = "Surname:";
			// 
			// TextBoxName
			// 
			this.TextBoxName.Location = new System.Drawing.Point(112, 64);
			this.TextBoxName.MaxLength = 50;
			this.TextBoxName.Name = "TextBoxName";
			this.TextBoxName.TabIndex = 3;
			this.TextBoxName.Text = "";
			// 
			// LabelUserName
			// 
			this.LabelUserName.Location = new System.Drawing.Point(16, 64);
			this.LabelUserName.Name = "LabelUserName";
			this.LabelUserName.Size = new System.Drawing.Size(64, 16);
			this.LabelUserName.TabIndex = 2;
			this.LabelUserName.Text = "Name: ";
			// 
			// TextBoxAlias
			// 
			this.TextBoxAlias.Location = new System.Drawing.Point(112, 32);
			this.TextBoxAlias.MaxLength = 50;
			this.TextBoxAlias.Name = "TextBoxAlias";
			this.TextBoxAlias.TabIndex = 1;
			this.TextBoxAlias.Text = "";
			this.TextBoxAlias.Validating += new System.ComponentModel.CancelEventHandler(this.TextBoxAlias_Validating);
			// 
			// LabelAlias
			// 
			this.LabelAlias.Location = new System.Drawing.Point(16, 32);
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
			this.ButtonPanel.Location = new System.Drawing.Point(0, 200);
			this.ButtonPanel.Name = "ButtonPanel";
			this.ButtonPanel.Size = new System.Drawing.Size(258, 40);
			this.ButtonPanel.TabIndex = 1;
			// 
			// ButtonCancel
			// 
			this.ButtonCancel.DialogResult = System.Windows.Forms.DialogResult.Cancel;
			this.ButtonCancel.Location = new System.Drawing.Point(144, 8);
			this.ButtonCancel.Name = "ButtonCancel";
			this.ButtonCancel.TabIndex = 1;
			this.ButtonCancel.Text = "Cancel";
			// 
			// ButtonOK
			// 
			this.ButtonOK.DialogResult = System.Windows.Forms.DialogResult.OK;
			this.ButtonOK.Location = new System.Drawing.Point(40, 8);
			this.ButtonOK.Name = "ButtonOK";
			this.ButtonOK.TabIndex = 0;
			this.ButtonOK.Text = "OK";
			// 
			// ErrorProvider
			// 
			this.ErrorProvider.BlinkStyle = System.Windows.Forms.ErrorBlinkStyle.NeverBlink;
			this.ErrorProvider.ContainerControl = this;
			// 
			// NewUserDialog
			// 
			this.AutoScaleBaseSize = new System.Drawing.Size(5, 13);
			this.ClientSize = new System.Drawing.Size(258, 240);
			this.Controls.Add(this.ButtonPanel);
			this.Controls.Add(this.ContentsPanel);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
			this.Name = "NewUserDialog";
			this.Text = "New User Dialog";
			this.ContentsPanel.ResumeLayout(false);
			this.ButtonPanel.ResumeLayout(false);
			this.ResumeLayout(false);

		}
		#endregion

		private void TextBoxAlias_Validating(object sender, 
			System.ComponentModel.CancelEventArgs e)
		{
			if (TextBoxAlias.Text != null && 
				aliasRegex.IsMatch(TextBoxAlias.Text))
			{
				ErrorProvider.SetError(TextBoxAlias, "");
			}
			else
			{
				ErrorProvider.SetError(TextBoxAlias, 
					@"User alias contains only numbers, digits and '-', '.', '_'!");
				e.Cancel = true;
			}
		}

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

		private void TextBoxPasswordAgain_Validating(object sender, 
			System.ComponentModel.CancelEventArgs e)
		{
			if (TextBoxPasswordAgain.Text == null || TextBoxPasswordAgain.Text.Length < 3)
			{
				ErrorProvider.SetError(TextBoxPasswordAgain, 
					@"Password should have at least 3 characters!");
				e.Cancel = true;
			} 
			else if (!TextBoxPasswordAgain.Text.Equals(TextBoxPassword.Text)) 
			{
				ErrorProvider.SetError(TextBoxPasswordAgain, 
					@"Repeted password is not the same!");
				e.Cancel = true;
			}
			else
			{
				ErrorProvider.SetError(TextBoxPasswordAgain, "");
			}
		}

	}
}
