using System;
using System.Drawing;
using System.Collections;
using System.ComponentModel;
using System.Windows.Forms;
using System.Text.RegularExpressions;

namespace PhotoAlbumGUI.Dialogs
{
	/// <summary>
	/// Summary description for LoginDialog.
	/// </summary>
	public class LoginDialog : System.Windows.Forms.Form
	{
		private System.Windows.Forms.Panel LoginButtonPanel;
		private System.Windows.Forms.Panel LoginContentPanel;
		private System.Windows.Forms.Label LabelLoginame;
		private System.Windows.Forms.Label LabelPassword;
		private System.Windows.Forms.TextBox TextBoxLoginame;
		private System.Windows.Forms.TextBox TextBoxPassword;
		private System.Windows.Forms.Button ButtonLoginOK;
		private System.Windows.Forms.Button ButtonCancelLogin;
		private System.Windows.Forms.ErrorProvider ErrorProvider;
		/// <summary>
		/// Required designer variable.
		/// </summary>
		private System.ComponentModel.Container components = null;
		private Regex aliasRegex = new Regex(@"^[A-Za-z0-9_\-\.]{0,50}$");

		internal string Loginame 
		{
			get
			{
				return TextBoxLoginame.Text;
			}
		}

		internal string Password 
		{
			get 
			{
				return TextBoxPassword.Text;
			}
		}

		public LoginDialog()
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
				LoginButtonPanel.Dispose();
				LoginContentPanel.Dispose();
				LabelLoginame.Dispose();
				LabelPassword.Dispose();
				TextBoxLoginame.Dispose();
				TextBoxPassword.Dispose();
				ButtonLoginOK.Dispose();
				ButtonCancelLogin.Dispose();
				ErrorProvider.Dispose();
				aliasRegex = null;
			}
			base.Dispose(disposing);
		}

		#region Windows Form Designer generated code
		/// <summary>
		/// Required method for Designer support - do not modify
		/// the contents of this method with the code editor.
		/// </summary>
		private void InitializeComponent()
		{
			this.LoginButtonPanel = new System.Windows.Forms.Panel();
			this.ButtonCancelLogin = new System.Windows.Forms.Button();
			this.ButtonLoginOK = new System.Windows.Forms.Button();
			this.LoginContentPanel = new System.Windows.Forms.Panel();
			this.TextBoxPassword = new System.Windows.Forms.TextBox();
			this.TextBoxLoginame = new System.Windows.Forms.TextBox();
			this.LabelPassword = new System.Windows.Forms.Label();
			this.LabelLoginame = new System.Windows.Forms.Label();
			this.ErrorProvider = new System.Windows.Forms.ErrorProvider();
			this.LoginButtonPanel.SuspendLayout();
			this.LoginContentPanel.SuspendLayout();
			this.SuspendLayout();
			// 
			// LoginButtonPanel
			// 
			this.LoginButtonPanel.Controls.Add(this.ButtonCancelLogin);
			this.LoginButtonPanel.Controls.Add(this.ButtonLoginOK);
			this.LoginButtonPanel.Dock = System.Windows.Forms.DockStyle.Bottom;
			this.LoginButtonPanel.Location = new System.Drawing.Point(0, 64);
			this.LoginButtonPanel.Name = "LoginButtonPanel";
			this.LoginButtonPanel.Size = new System.Drawing.Size(250, 40);
			this.LoginButtonPanel.TabIndex = 0;
			// 
			// ButtonCancelLogin
			// 
			this.ButtonCancelLogin.DialogResult = System.Windows.Forms.DialogResult.Cancel;
			this.ButtonCancelLogin.Location = new System.Drawing.Point(136, 8);
			this.ButtonCancelLogin.Name = "ButtonCancelLogin";
			this.ButtonCancelLogin.TabIndex = 1;
			this.ButtonCancelLogin.Text = "Cancel";
			// 
			// ButtonLoginOK
			// 
			this.ButtonLoginOK.DialogResult = System.Windows.Forms.DialogResult.OK;
			this.ButtonLoginOK.Location = new System.Drawing.Point(40, 8);
			this.ButtonLoginOK.Name = "ButtonLoginOK";
			this.ButtonLoginOK.TabIndex = 0;
			this.ButtonLoginOK.Text = "OK";
			// 
			// LoginContentPanel
			// 
			this.LoginContentPanel.Controls.Add(this.TextBoxPassword);
			this.LoginContentPanel.Controls.Add(this.TextBoxLoginame);
			this.LoginContentPanel.Controls.Add(this.LabelPassword);
			this.LoginContentPanel.Controls.Add(this.LabelLoginame);
			this.LoginContentPanel.Dock = System.Windows.Forms.DockStyle.Fill;
			this.LoginContentPanel.Location = new System.Drawing.Point(0, 0);
			this.LoginContentPanel.Name = "LoginContentPanel";
			this.LoginContentPanel.Size = new System.Drawing.Size(250, 64);
			this.LoginContentPanel.TabIndex = 1;
			// 
			// TextBoxPassword
			// 
			this.TextBoxPassword.Location = new System.Drawing.Point(88, 40);
			this.TextBoxPassword.MaxLength = 50;
			this.TextBoxPassword.Name = "TextBoxPassword";
			this.TextBoxPassword.PasswordChar = '*';
			this.TextBoxPassword.Size = new System.Drawing.Size(128, 20);
			this.TextBoxPassword.TabIndex = 3;
			this.TextBoxPassword.Text = "";
			this.TextBoxPassword.Validating += new System.ComponentModel.CancelEventHandler(this.TextBoxPassword_Validating);
			// 
			// TextBoxLoginame
			// 
			this.TextBoxLoginame.Location = new System.Drawing.Point(88, 16);
			this.TextBoxLoginame.MaxLength = 50;
			this.TextBoxLoginame.Name = "TextBoxLoginame";
			this.TextBoxLoginame.Size = new System.Drawing.Size(128, 20);
			this.TextBoxLoginame.TabIndex = 1;
			this.TextBoxLoginame.Text = "";
			this.TextBoxLoginame.Validating += new System.ComponentModel.CancelEventHandler(this.TextBoxLoginame_Validating);
			// 
			// LabelPassword
			// 
			this.LabelPassword.Location = new System.Drawing.Point(8, 40);
			this.LabelPassword.Name = "LabelPassword";
			this.LabelPassword.Size = new System.Drawing.Size(64, 16);
			this.LabelPassword.TabIndex = 2;
			this.LabelPassword.Text = "Password:";
			// 
			// LabelLoginame
			// 
			this.LabelLoginame.Location = new System.Drawing.Point(8, 16);
			this.LabelLoginame.Name = "LabelLoginame";
			this.LabelLoginame.Size = new System.Drawing.Size(64, 16);
			this.LabelLoginame.TabIndex = 0;
			this.LabelLoginame.Text = "Loginame:";
			// 
			// ErrorProvider
			// 
			this.ErrorProvider.BlinkStyle = System.Windows.Forms.ErrorBlinkStyle.NeverBlink;
			this.ErrorProvider.ContainerControl = this;
			// 
			// LoginDialog
			// 
			this.AutoScaleBaseSize = new System.Drawing.Size(5, 13);
			this.ClientSize = new System.Drawing.Size(250, 104);
			this.Controls.Add(this.LoginContentPanel);
			this.Controls.Add(this.LoginButtonPanel);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
			this.Location = new System.Drawing.Point(550, 450);
			this.MaximizeBox = false;
			this.MinimizeBox = false;
			this.Name = "LoginDialog";
			this.Text = "Login Dialog";
			this.LoginButtonPanel.ResumeLayout(false);
			this.LoginContentPanel.ResumeLayout(false);
			this.ResumeLayout(false);

		}
		#endregion

		
		private void TextBoxLoginame_Validating(object sender, 
			System.ComponentModel.CancelEventArgs e)
		{
			if (TextBoxLoginame.Text != null && 
				aliasRegex.IsMatch(TextBoxLoginame.Text))
			{
				ErrorProvider.SetError(TextBoxLoginame, "");
			}
			else
			{
				ErrorProvider.SetError(TextBoxLoginame, 
					@"Only numbers, digits and '-', '.', '_' are allowed!");
				e.Cancel = true;
			}
		}

		private void TextBoxPassword_Validating(object sender, 
			System.ComponentModel.CancelEventArgs e)
		{
			if (TextBoxPassword.Text != null && 
				TextBoxPassword.Text.Length >= 3)
			{
				ErrorProvider.SetError(TextBoxPassword, "");
			}
			else
			{
				ErrorProvider.SetError(TextBoxPassword, 
					@"Password should have at least 3 characters!");
				e.Cancel = true;
			}
		}

	}
}
