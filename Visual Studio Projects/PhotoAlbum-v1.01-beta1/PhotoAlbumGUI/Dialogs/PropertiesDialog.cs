using System;
using System.Drawing;
using System.Collections;
using System.ComponentModel;
using System.Windows.Forms;

namespace PhotoAlbumGUI.Dialogs
{
	/// <summary>
	/// Summary description for PropertiesDialog.
	/// </summary>
	public class PropertiesDialog : System.Windows.Forms.Form
	{
		private System.Windows.Forms.Panel ButtonsPanel;
		private System.Windows.Forms.Label LabelName;
		private System.Windows.Forms.TextBox TextBoxName;
		private System.Windows.Forms.Label LabelDescription;
		private System.Windows.Forms.RichTextBox RichTextBoxDescription;
		private System.Windows.Forms.Label LabelTextOwner;
		private System.Windows.Forms.Label LabelTextCreated;
		private System.Windows.Forms.Label LabelTextSize;
		private System.Windows.Forms.Label LabelOwner;
		private System.Windows.Forms.Label LabelCreated;
		private System.Windows.Forms.Label LabelSize;
		private System.Windows.Forms.CheckBox CheckBoxShared;
		private System.Windows.Forms.Button ButtonOK;
		private System.Windows.Forms.Button ButtonCancel;
		private System.Windows.Forms.Panel ContentsPanel;
		/// <summary>
		/// Required designer variable.
		/// </summary>
		private System.ComponentModel.Container components = null;

		internal string ItemName 
		{
			get 
			{
				return TextBoxName.Text;
			}
			set 
			{
				TextBoxName.Text = value;
			}
		}

		internal string ItemOwner 
		{
			set 
			{
				LabelOwner.Text = value;
			}
		}

		internal string ItemCreated 
		{
			set 
			{
				LabelCreated.Text = value;
			}
		}

		internal string ItemSize 
		{
			set 
			{
				LabelSize.Text = value;
			}
		}

		internal bool ItemShared 
		{
			set 
			{
				if (value) 
				{
					CheckBoxShared.CheckState = CheckState.Checked;
				}
				else 
				{
					CheckBoxShared.CheckState = CheckState.Unchecked;
				}
			}
		}

		internal string ItemDescription 
		{
			get 
			{
				return RichTextBoxDescription.Text;
			}

			set 
			{
				RichTextBoxDescription.Visible = true;
				LabelDescription.Visible = true;
				RichTextBoxDescription.Text = value;
			}
		}

		public PropertiesDialog()
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
				ContentsPanel.Dispose();
				ButtonsPanel.Dispose();
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
			this.CheckBoxShared = new System.Windows.Forms.CheckBox();
			this.LabelSize = new System.Windows.Forms.Label();
			this.LabelCreated = new System.Windows.Forms.Label();
			this.LabelOwner = new System.Windows.Forms.Label();
			this.LabelTextSize = new System.Windows.Forms.Label();
			this.LabelTextCreated = new System.Windows.Forms.Label();
			this.LabelTextOwner = new System.Windows.Forms.Label();
			this.RichTextBoxDescription = new System.Windows.Forms.RichTextBox();
			this.LabelDescription = new System.Windows.Forms.Label();
			this.TextBoxName = new System.Windows.Forms.TextBox();
			this.LabelName = new System.Windows.Forms.Label();
			this.ButtonsPanel = new System.Windows.Forms.Panel();
			this.ButtonCancel = new System.Windows.Forms.Button();
			this.ButtonOK = new System.Windows.Forms.Button();
			this.ContentsPanel.SuspendLayout();
			this.ButtonsPanel.SuspendLayout();
			this.SuspendLayout();
			// 
			// ContentsPanel
			// 
			this.ContentsPanel.Controls.Add(this.CheckBoxShared);
			this.ContentsPanel.Controls.Add(this.LabelSize);
			this.ContentsPanel.Controls.Add(this.LabelCreated);
			this.ContentsPanel.Controls.Add(this.LabelOwner);
			this.ContentsPanel.Controls.Add(this.LabelTextSize);
			this.ContentsPanel.Controls.Add(this.LabelTextCreated);
			this.ContentsPanel.Controls.Add(this.LabelTextOwner);
			this.ContentsPanel.Controls.Add(this.RichTextBoxDescription);
			this.ContentsPanel.Controls.Add(this.LabelDescription);
			this.ContentsPanel.Controls.Add(this.TextBoxName);
			this.ContentsPanel.Controls.Add(this.LabelName);
			this.ContentsPanel.Dock = System.Windows.Forms.DockStyle.Fill;
			this.ContentsPanel.Location = new System.Drawing.Point(0, 0);
			this.ContentsPanel.Name = "ContentsPanel";
			this.ContentsPanel.Size = new System.Drawing.Size(226, 272);
			this.ContentsPanel.TabIndex = 0;
			// 
			// CheckBoxShared
			// 
			this.CheckBoxShared.Enabled = false;
			this.CheckBoxShared.Location = new System.Drawing.Point(8, 128);
			this.CheckBoxShared.Name = "CheckBoxShared";
			this.CheckBoxShared.Size = new System.Drawing.Size(72, 24);
			this.CheckBoxShared.TabIndex = 8;
			this.CheckBoxShared.TabStop = false;
			this.CheckBoxShared.Text = "Shared";
			// 
			// LabelSize
			// 
			this.LabelSize.Location = new System.Drawing.Point(88, 96);
			this.LabelSize.Name = "LabelSize";
			this.LabelSize.Size = new System.Drawing.Size(100, 16);
			this.LabelSize.TabIndex = 7;
			// 
			// LabelCreated
			// 
			this.LabelCreated.Location = new System.Drawing.Point(88, 72);
			this.LabelCreated.Name = "LabelCreated";
			this.LabelCreated.Size = new System.Drawing.Size(120, 16);
			this.LabelCreated.TabIndex = 5;
			// 
			// LabelOwner
			// 
			this.LabelOwner.Location = new System.Drawing.Point(88, 48);
			this.LabelOwner.Name = "LabelOwner";
			this.LabelOwner.Size = new System.Drawing.Size(120, 16);
			this.LabelOwner.TabIndex = 3;
			// 
			// LabelTextSize
			// 
			this.LabelTextSize.Location = new System.Drawing.Point(8, 96);
			this.LabelTextSize.Name = "LabelTextSize";
			this.LabelTextSize.Size = new System.Drawing.Size(72, 16);
			this.LabelTextSize.TabIndex = 6;
			this.LabelTextSize.Text = "Size:";
			// 
			// LabelTextCreated
			// 
			this.LabelTextCreated.Location = new System.Drawing.Point(8, 72);
			this.LabelTextCreated.Name = "LabelTextCreated";
			this.LabelTextCreated.Size = new System.Drawing.Size(72, 16);
			this.LabelTextCreated.TabIndex = 4;
			this.LabelTextCreated.Text = "Created:";
			// 
			// LabelTextOwner
			// 
			this.LabelTextOwner.Location = new System.Drawing.Point(8, 48);
			this.LabelTextOwner.Name = "LabelTextOwner";
			this.LabelTextOwner.Size = new System.Drawing.Size(72, 16);
			this.LabelTextOwner.TabIndex = 2;
			this.LabelTextOwner.Text = "Owner:";
			// 
			// RichTextBoxDescription
			// 
			this.RichTextBoxDescription.Location = new System.Drawing.Point(88, 168);
			this.RichTextBoxDescription.MaxLength = 214748;
			this.RichTextBoxDescription.Name = "RichTextBoxDescription";
			this.RichTextBoxDescription.Size = new System.Drawing.Size(120, 56);
			this.RichTextBoxDescription.TabIndex = 10;
			this.RichTextBoxDescription.Text = "";
			this.RichTextBoxDescription.Visible = false;
			// 
			// LabelDescription
			// 
			this.LabelDescription.Location = new System.Drawing.Point(8, 168);
			this.LabelDescription.Name = "LabelDescription";
			this.LabelDescription.Size = new System.Drawing.Size(72, 16);
			this.LabelDescription.TabIndex = 9;
			this.LabelDescription.Text = "Description:";
			this.LabelDescription.Visible = false;
			// 
			// TextBoxName
			// 
			this.TextBoxName.Location = new System.Drawing.Point(88, 8);
			this.TextBoxName.MaxLength = 50;
			this.TextBoxName.Name = "TextBoxName";
			this.TextBoxName.Size = new System.Drawing.Size(120, 20);
			this.TextBoxName.TabIndex = 1;
			this.TextBoxName.Text = "";
			// 
			// LabelName
			// 
			this.LabelName.Location = new System.Drawing.Point(8, 8);
			this.LabelName.Name = "LabelName";
			this.LabelName.Size = new System.Drawing.Size(72, 16);
			this.LabelName.TabIndex = 0;
			this.LabelName.Text = "Name:";
			// 
			// ButtonsPanel
			// 
			this.ButtonsPanel.Controls.Add(this.ButtonCancel);
			this.ButtonsPanel.Controls.Add(this.ButtonOK);
			this.ButtonsPanel.Dock = System.Windows.Forms.DockStyle.Bottom;
			this.ButtonsPanel.Location = new System.Drawing.Point(0, 232);
			this.ButtonsPanel.Name = "ButtonsPanel";
			this.ButtonsPanel.Size = new System.Drawing.Size(226, 40);
			this.ButtonsPanel.TabIndex = 1;
			// 
			// ButtonCancel
			// 
			this.ButtonCancel.DialogResult = System.Windows.Forms.DialogResult.Cancel;
			this.ButtonCancel.Location = new System.Drawing.Point(112, 8);
			this.ButtonCancel.Name = "ButtonCancel";
			this.ButtonCancel.TabIndex = 1;
			this.ButtonCancel.Text = "Cancel";
			// 
			// ButtonOK
			// 
			this.ButtonOK.DialogResult = System.Windows.Forms.DialogResult.OK;
			this.ButtonOK.Location = new System.Drawing.Point(16, 8);
			this.ButtonOK.Name = "ButtonOK";
			this.ButtonOK.TabIndex = 0;
			this.ButtonOK.Text = "OK";
			// 
			// PropertiesDialog
			// 
			this.AutoScaleBaseSize = new System.Drawing.Size(5, 13);
			this.ClientSize = new System.Drawing.Size(226, 272);
			this.Controls.Add(this.ButtonsPanel);
			this.Controls.Add(this.ContentsPanel);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
			this.Name = "PropertiesDialog";
			this.Text = "Properties Dialog";
			this.ContentsPanel.ResumeLayout(false);
			this.ButtonsPanel.ResumeLayout(false);
			this.ResumeLayout(false);

		}
		#endregion
	}
}
