using System;
using System.IO;
using System.Drawing;
using System.Threading;
using System.Collections;
using System.ComponentModel;
using System.Windows.Forms;
using PhotoAlbumDB.Entities;
using PhotoAlbumDB.Exceptions;
using PhotoAlbumGUI.Dialogs;

namespace PhotoUITesting
{
	/// <summary>
	/// Container class for the Graffical User 
	/// Interface of the application. 
	/// </summary>
	public sealed class PhotoMain : 
		System.Windows.Forms.Form, PhotoAlbumDB.Entities.Image.ImageViewer
	{
		private System.Windows.Forms.MenuItem MenuItemFile;
		private System.Windows.Forms.MenuItem MenuItemFileOpen;
		private System.Windows.Forms.MenuItem MenuItemUsers;
		private System.Windows.Forms.MenuItem MenuItemChangePassword;
		private System.Windows.Forms.MenuItem MenuItemChangeProfile;
		private System.Windows.Forms.MenuItem MenuItemLine1;
		private System.Windows.Forms.MenuItem MenuItemLine3;
		private System.Windows.Forms.MenuItem menuItemLine2;
		private System.Windows.Forms.MenuItem menuItemExit;
		private System.Windows.Forms.MenuItem MenuItemNewUser;
		private System.Windows.Forms.MenuItem MenuItemDeleteUser;
		private System.Windows.Forms.MenuItem MenuItemSysEntry;
		private System.Windows.Forms.MenuItem MenuItemLogin;
		private System.Windows.Forms.MenuItem MenuItemLogout;
		private System.Windows.Forms.MenuItem MenuItemAddFolder;
		private System.Windows.Forms.MenuItem MenuItemDeepAddFolder;
		private System.Windows.Forms.MenuItem MainMenuItemView;
		private System.Windows.Forms.MenuItem MenuItemPersonal;
		private System.Windows.Forms.MenuItem MainMenuItemHelp;
		private System.Windows.Forms.MenuItem MenuItemContents;
		private System.Windows.Forms.MenuItem MenuItemAbout;
		private System.Windows.Forms.MenuItem MenuItemShared;
		private System.Windows.Forms.MainMenu MainFormMenu;
		private System.Windows.Forms.TreeView MainFormTreeView;
		private System.Windows.Forms.ImageList ImageListTreeIcons;
		private System.Windows.Forms.Splitter MainFormSplitter;
		private System.Windows.Forms.ListView MainFrameListView;
		private System.Windows.Forms.ImageList ListViewImageList;
		private System.Windows.Forms.ContextMenu TreeViewContextMenu;
		private System.Windows.Forms.OpenFileDialog OpenImageFileDialog;
		private System.Windows.Forms.FolderBrowserDialog FolderChooserDialog;
		private System.ComponentModel.IContainer components;
		private System.Drawing.Point treeMouseLocation = new Point(0, 0);
		private User currentUser;
		private Thread mFileLoaderThread = null;

		public PhotoMain()
		{
			InitializeComponent();
			try 
			{
				Icon ico = new Icon("icons\\folder.ico");
				ImageListTreeIcons.Images.Add(ico);
				ico = new Icon("icons\\folderOpened.ico");
				ImageListTreeIcons.Images.Add(ico);
				ico = new Icon("icons\\picture.ico");
				ImageListTreeIcons.Images.Add(ico);
				this.Icon = new Icon("icons\\tree.ico");
			} 
			catch (FileNotFoundException) 
			{
				MessageBox.Show("Could not load application icons.", "Info", 
					MessageBoxButtons.OK, MessageBoxIcon.Information);
			}
			catch (Exception) 
			{
				MessageBox.Show("Could not load application icons.", "Info", 
					MessageBoxButtons.OK, MessageBoxIcon.Information);
			}
		}

		public void viewImage(PhotoAlbumDB.Entities.Image image) 
		{
			ImageViewerDialog dialog = new ImageViewerDialog();
			byte[] binary = image.Binary;
			MemoryStream ms = new MemoryStream(binary, 0, binary.Length);
			using (ms) 
			{
				System.Drawing.Image picture = 
					System.Drawing.Image.FromStream(ms);
				dialog.Image = picture;
				dialog.Show();
			}
			binary = null;
			System.GC.Collect();
		}

		public void viewThumbnail(PhotoAlbumDB.Entities.Image image) 
		{
			// display the image			
			ListViewItem newItem = new ListViewItem(image.Name);
			newItem.Tag = image;

			byte[] binary = image.Binary;
			MemoryStream ms = new MemoryStream(binary, 0, binary.Length);
			using (ms)
			{
				System.Drawing.Image picture = System.Drawing.Image.FromStream(ms);			
				picture = picture.GetThumbnailImage(120, 100, null, IntPtr.Zero);
				int index = ListViewImageList.Images.Add(picture, Color.White);
			
				newItem.ImageIndex = index;
				MainFrameListView.Items.Add(newItem);
				MainFrameListView.Update();//Refresh();
			}
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
				if (currentUser != null) 
				{
					currentUser.Dispose();
					currentUser = null;
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
			this.components = new System.ComponentModel.Container();
			System.Resources.ResourceManager resources = new System.Resources.ResourceManager(typeof(PhotoMain));
			this.MainFormMenu = new System.Windows.Forms.MainMenu();
			this.MenuItemFile = new System.Windows.Forms.MenuItem();
			this.MenuItemFileOpen = new System.Windows.Forms.MenuItem();
			this.MenuItemLine1 = new System.Windows.Forms.MenuItem();
			this.MenuItemAddFolder = new System.Windows.Forms.MenuItem();
			this.MenuItemDeepAddFolder = new System.Windows.Forms.MenuItem();
			this.menuItemLine2 = new System.Windows.Forms.MenuItem();
			this.menuItemExit = new System.Windows.Forms.MenuItem();
			this.MenuItemUsers = new System.Windows.Forms.MenuItem();
			this.MenuItemChangePassword = new System.Windows.Forms.MenuItem();
			this.MenuItemChangeProfile = new System.Windows.Forms.MenuItem();
			this.MenuItemLine3 = new System.Windows.Forms.MenuItem();
			this.MenuItemNewUser = new System.Windows.Forms.MenuItem();
			this.MenuItemDeleteUser = new System.Windows.Forms.MenuItem();
			this.MenuItemSysEntry = new System.Windows.Forms.MenuItem();
			this.MenuItemLogin = new System.Windows.Forms.MenuItem();
			this.MenuItemLogout = new System.Windows.Forms.MenuItem();
			this.MainMenuItemView = new System.Windows.Forms.MenuItem();
			this.MenuItemPersonal = new System.Windows.Forms.MenuItem();
			this.MenuItemShared = new System.Windows.Forms.MenuItem();
			this.MainMenuItemHelp = new System.Windows.Forms.MenuItem();
			this.MenuItemContents = new System.Windows.Forms.MenuItem();
			this.MenuItemAbout = new System.Windows.Forms.MenuItem();
			this.MainFormTreeView = new System.Windows.Forms.TreeView();
			this.TreeViewContextMenu = new System.Windows.Forms.ContextMenu();
			this.ImageListTreeIcons = new System.Windows.Forms.ImageList(this.components);
			this.MainFormSplitter = new System.Windows.Forms.Splitter();
			this.OpenImageFileDialog = new System.Windows.Forms.OpenFileDialog();
			this.FolderChooserDialog = new System.Windows.Forms.FolderBrowserDialog();
			this.MainFrameListView = new System.Windows.Forms.ListView();
			this.ListViewImageList = new System.Windows.Forms.ImageList(this.components);
			this.SuspendLayout();
			// 
			// MainFormMenu
			// 
			this.MainFormMenu.MenuItems.AddRange(new System.Windows.Forms.MenuItem[] {
																						 this.MenuItemFile,
																						 this.MenuItemUsers,
																						 this.MenuItemSysEntry,
																						 this.MainMenuItemView,
																						 this.MainMenuItemHelp});
			this.MainFormMenu.RightToLeft = ((System.Windows.Forms.RightToLeft)(resources.GetObject("MainFormMenu.RightToLeft")));
			// 
			// MenuItemFile
			// 
			this.MenuItemFile.Enabled = ((bool)(resources.GetObject("MenuItemFile.Enabled")));
			this.MenuItemFile.Index = 0;
			this.MenuItemFile.MenuItems.AddRange(new System.Windows.Forms.MenuItem[] {
																						 this.MenuItemFileOpen,
																						 this.MenuItemLine1,
																						 this.MenuItemAddFolder,
																						 this.MenuItemDeepAddFolder,
																						 this.menuItemLine2,
																						 this.menuItemExit});
			this.MenuItemFile.Shortcut = ((System.Windows.Forms.Shortcut)(resources.GetObject("MenuItemFile.Shortcut")));
			this.MenuItemFile.ShowShortcut = ((bool)(resources.GetObject("MenuItemFile.ShowShortcut")));
			this.MenuItemFile.Text = resources.GetString("MenuItemFile.Text");
			this.MenuItemFile.Visible = ((bool)(resources.GetObject("MenuItemFile.Visible")));
			// 
			// MenuItemFileOpen
			// 
			this.MenuItemFileOpen.Enabled = ((bool)(resources.GetObject("MenuItemFileOpen.Enabled")));
			this.MenuItemFileOpen.Index = 0;
			this.MenuItemFileOpen.Shortcut = ((System.Windows.Forms.Shortcut)(resources.GetObject("MenuItemFileOpen.Shortcut")));
			this.MenuItemFileOpen.ShowShortcut = ((bool)(resources.GetObject("MenuItemFileOpen.ShowShortcut")));
			this.MenuItemFileOpen.Text = resources.GetString("MenuItemFileOpen.Text");
			this.MenuItemFileOpen.Visible = ((bool)(resources.GetObject("MenuItemFileOpen.Visible")));
			// 
			// MenuItemLine1
			// 
			this.MenuItemLine1.Enabled = ((bool)(resources.GetObject("MenuItemLine1.Enabled")));
			this.MenuItemLine1.Index = 1;
			this.MenuItemLine1.Shortcut = ((System.Windows.Forms.Shortcut)(resources.GetObject("MenuItemLine1.Shortcut")));
			this.MenuItemLine1.ShowShortcut = ((bool)(resources.GetObject("MenuItemLine1.ShowShortcut")));
			this.MenuItemLine1.Text = resources.GetString("MenuItemLine1.Text");
			this.MenuItemLine1.Visible = ((bool)(resources.GetObject("MenuItemLine1.Visible")));
			// 
			// MenuItemAddFolder
			// 
			this.MenuItemAddFolder.Enabled = ((bool)(resources.GetObject("MenuItemAddFolder.Enabled")));
			this.MenuItemAddFolder.Index = 2;
			this.MenuItemAddFolder.Shortcut = ((System.Windows.Forms.Shortcut)(resources.GetObject("MenuItemAddFolder.Shortcut")));
			this.MenuItemAddFolder.ShowShortcut = ((bool)(resources.GetObject("MenuItemAddFolder.ShowShortcut")));
			this.MenuItemAddFolder.Text = resources.GetString("MenuItemAddFolder.Text");
			this.MenuItemAddFolder.Visible = ((bool)(resources.GetObject("MenuItemAddFolder.Visible")));
			this.MenuItemAddFolder.Click += new System.EventHandler(this.MenuItemAddFolder_Click);
			// 
			// MenuItemDeepAddFolder
			// 
			this.MenuItemDeepAddFolder.Enabled = ((bool)(resources.GetObject("MenuItemDeepAddFolder.Enabled")));
			this.MenuItemDeepAddFolder.Index = 3;
			this.MenuItemDeepAddFolder.Shortcut = ((System.Windows.Forms.Shortcut)(resources.GetObject("MenuItemDeepAddFolder.Shortcut")));
			this.MenuItemDeepAddFolder.ShowShortcut = ((bool)(resources.GetObject("MenuItemDeepAddFolder.ShowShortcut")));
			this.MenuItemDeepAddFolder.Text = resources.GetString("MenuItemDeepAddFolder.Text");
			this.MenuItemDeepAddFolder.Visible = ((bool)(resources.GetObject("MenuItemDeepAddFolder.Visible")));
			// 
			// menuItemLine2
			// 
			this.menuItemLine2.Enabled = ((bool)(resources.GetObject("menuItemLine2.Enabled")));
			this.menuItemLine2.Index = 4;
			this.menuItemLine2.Shortcut = ((System.Windows.Forms.Shortcut)(resources.GetObject("menuItemLine2.Shortcut")));
			this.menuItemLine2.ShowShortcut = ((bool)(resources.GetObject("menuItemLine2.ShowShortcut")));
			this.menuItemLine2.Text = resources.GetString("menuItemLine2.Text");
			this.menuItemLine2.Visible = ((bool)(resources.GetObject("menuItemLine2.Visible")));
			// 
			// menuItemExit
			// 
			this.menuItemExit.Enabled = ((bool)(resources.GetObject("menuItemExit.Enabled")));
			this.menuItemExit.Index = 5;
			this.menuItemExit.Shortcut = ((System.Windows.Forms.Shortcut)(resources.GetObject("menuItemExit.Shortcut")));
			this.menuItemExit.ShowShortcut = ((bool)(resources.GetObject("menuItemExit.ShowShortcut")));
			this.menuItemExit.Text = resources.GetString("menuItemExit.Text");
			this.menuItemExit.Visible = ((bool)(resources.GetObject("menuItemExit.Visible")));
			this.menuItemExit.Click += new System.EventHandler(this.MenuItemExit_Click);
			// 
			// MenuItemUsers
			// 
			this.MenuItemUsers.Enabled = ((bool)(resources.GetObject("MenuItemUsers.Enabled")));
			this.MenuItemUsers.Index = 1;
			this.MenuItemUsers.MenuItems.AddRange(new System.Windows.Forms.MenuItem[] {
																						  this.MenuItemChangePassword,
																						  this.MenuItemChangeProfile,
																						  this.MenuItemLine3,
																						  this.MenuItemNewUser,
																						  this.MenuItemDeleteUser});
			this.MenuItemUsers.Shortcut = ((System.Windows.Forms.Shortcut)(resources.GetObject("MenuItemUsers.Shortcut")));
			this.MenuItemUsers.ShowShortcut = ((bool)(resources.GetObject("MenuItemUsers.ShowShortcut")));
			this.MenuItemUsers.Text = resources.GetString("MenuItemUsers.Text");
			this.MenuItemUsers.Visible = ((bool)(resources.GetObject("MenuItemUsers.Visible")));
			// 
			// MenuItemChangePassword
			// 
			this.MenuItemChangePassword.Enabled = ((bool)(resources.GetObject("MenuItemChangePassword.Enabled")));
			this.MenuItemChangePassword.Index = 0;
			this.MenuItemChangePassword.Shortcut = ((System.Windows.Forms.Shortcut)(resources.GetObject("MenuItemChangePassword.Shortcut")));
			this.MenuItemChangePassword.ShowShortcut = ((bool)(resources.GetObject("MenuItemChangePassword.ShowShortcut")));
			this.MenuItemChangePassword.Text = resources.GetString("MenuItemChangePassword.Text");
			this.MenuItemChangePassword.Visible = ((bool)(resources.GetObject("MenuItemChangePassword.Visible")));
			this.MenuItemChangePassword.Click += new System.EventHandler(this.MenuItemChangePassword_Click);
			// 
			// MenuItemChangeProfile
			// 
			this.MenuItemChangeProfile.Enabled = ((bool)(resources.GetObject("MenuItemChangeProfile.Enabled")));
			this.MenuItemChangeProfile.Index = 1;
			this.MenuItemChangeProfile.Shortcut = ((System.Windows.Forms.Shortcut)(resources.GetObject("MenuItemChangeProfile.Shortcut")));
			this.MenuItemChangeProfile.ShowShortcut = ((bool)(resources.GetObject("MenuItemChangeProfile.ShowShortcut")));
			this.MenuItemChangeProfile.Text = resources.GetString("MenuItemChangeProfile.Text");
			this.MenuItemChangeProfile.Visible = ((bool)(resources.GetObject("MenuItemChangeProfile.Visible")));
			this.MenuItemChangeProfile.Click += new System.EventHandler(this.MenuItemChangeProfile_Click);
			// 
			// MenuItemLine3
			// 
			this.MenuItemLine3.Enabled = ((bool)(resources.GetObject("MenuItemLine3.Enabled")));
			this.MenuItemLine3.Index = 2;
			this.MenuItemLine3.Shortcut = ((System.Windows.Forms.Shortcut)(resources.GetObject("MenuItemLine3.Shortcut")));
			this.MenuItemLine3.ShowShortcut = ((bool)(resources.GetObject("MenuItemLine3.ShowShortcut")));
			this.MenuItemLine3.Text = resources.GetString("MenuItemLine3.Text");
			this.MenuItemLine3.Visible = ((bool)(resources.GetObject("MenuItemLine3.Visible")));
			// 
			// MenuItemNewUser
			// 
			this.MenuItemNewUser.Enabled = ((bool)(resources.GetObject("MenuItemNewUser.Enabled")));
			this.MenuItemNewUser.Index = 3;
			this.MenuItemNewUser.Shortcut = ((System.Windows.Forms.Shortcut)(resources.GetObject("MenuItemNewUser.Shortcut")));
			this.MenuItemNewUser.ShowShortcut = ((bool)(resources.GetObject("MenuItemNewUser.ShowShortcut")));
			this.MenuItemNewUser.Text = resources.GetString("MenuItemNewUser.Text");
			this.MenuItemNewUser.Visible = ((bool)(resources.GetObject("MenuItemNewUser.Visible")));
			this.MenuItemNewUser.Click += new System.EventHandler(this.MenuItemNewUser_Click);
			// 
			// MenuItemDeleteUser
			// 
			this.MenuItemDeleteUser.Enabled = ((bool)(resources.GetObject("MenuItemDeleteUser.Enabled")));
			this.MenuItemDeleteUser.Index = 4;
			this.MenuItemDeleteUser.Shortcut = ((System.Windows.Forms.Shortcut)(resources.GetObject("MenuItemDeleteUser.Shortcut")));
			this.MenuItemDeleteUser.ShowShortcut = ((bool)(resources.GetObject("MenuItemDeleteUser.ShowShortcut")));
			this.MenuItemDeleteUser.Text = resources.GetString("MenuItemDeleteUser.Text");
			this.MenuItemDeleteUser.Visible = ((bool)(resources.GetObject("MenuItemDeleteUser.Visible")));
			this.MenuItemDeleteUser.Click += new System.EventHandler(this.MenuItemDeleteUser_Click);
			// 
			// MenuItemSysEntry
			// 
			this.MenuItemSysEntry.Enabled = ((bool)(resources.GetObject("MenuItemSysEntry.Enabled")));
			this.MenuItemSysEntry.Index = 2;
			this.MenuItemSysEntry.MenuItems.AddRange(new System.Windows.Forms.MenuItem[] {
																							 this.MenuItemLogin,
																							 this.MenuItemLogout});
			this.MenuItemSysEntry.Shortcut = ((System.Windows.Forms.Shortcut)(resources.GetObject("MenuItemSysEntry.Shortcut")));
			this.MenuItemSysEntry.ShowShortcut = ((bool)(resources.GetObject("MenuItemSysEntry.ShowShortcut")));
			this.MenuItemSysEntry.Text = resources.GetString("MenuItemSysEntry.Text");
			this.MenuItemSysEntry.Visible = ((bool)(resources.GetObject("MenuItemSysEntry.Visible")));
			// 
			// MenuItemLogin
			// 
			this.MenuItemLogin.Enabled = ((bool)(resources.GetObject("MenuItemLogin.Enabled")));
			this.MenuItemLogin.Index = 0;
			this.MenuItemLogin.Shortcut = ((System.Windows.Forms.Shortcut)(resources.GetObject("MenuItemLogin.Shortcut")));
			this.MenuItemLogin.ShowShortcut = ((bool)(resources.GetObject("MenuItemLogin.ShowShortcut")));
			this.MenuItemLogin.Text = resources.GetString("MenuItemLogin.Text");
			this.MenuItemLogin.Visible = ((bool)(resources.GetObject("MenuItemLogin.Visible")));
			this.MenuItemLogin.Click += new System.EventHandler(this.MenuItemLogin_Click);
			// 
			// MenuItemLogout
			// 
			this.MenuItemLogout.Enabled = ((bool)(resources.GetObject("MenuItemLogout.Enabled")));
			this.MenuItemLogout.Index = 1;
			this.MenuItemLogout.Shortcut = ((System.Windows.Forms.Shortcut)(resources.GetObject("MenuItemLogout.Shortcut")));
			this.MenuItemLogout.ShowShortcut = ((bool)(resources.GetObject("MenuItemLogout.ShowShortcut")));
			this.MenuItemLogout.Text = resources.GetString("MenuItemLogout.Text");
			this.MenuItemLogout.Visible = ((bool)(resources.GetObject("MenuItemLogout.Visible")));
			this.MenuItemLogout.Click += new System.EventHandler(this.MenuItemLogout_Click);
			// 
			// MainMenuItemView
			// 
			this.MainMenuItemView.Enabled = ((bool)(resources.GetObject("MainMenuItemView.Enabled")));
			this.MainMenuItemView.Index = 3;
			this.MainMenuItemView.MenuItems.AddRange(new System.Windows.Forms.MenuItem[] {
																							 this.MenuItemPersonal,
																							 this.MenuItemShared});
			this.MainMenuItemView.Shortcut = ((System.Windows.Forms.Shortcut)(resources.GetObject("MainMenuItemView.Shortcut")));
			this.MainMenuItemView.ShowShortcut = ((bool)(resources.GetObject("MainMenuItemView.ShowShortcut")));
			this.MainMenuItemView.Text = resources.GetString("MainMenuItemView.Text");
			this.MainMenuItemView.Visible = ((bool)(resources.GetObject("MainMenuItemView.Visible")));
			// 
			// MenuItemPersonal
			// 
			this.MenuItemPersonal.Enabled = ((bool)(resources.GetObject("MenuItemPersonal.Enabled")));
			this.MenuItemPersonal.Index = 0;
			this.MenuItemPersonal.Shortcut = ((System.Windows.Forms.Shortcut)(resources.GetObject("MenuItemPersonal.Shortcut")));
			this.MenuItemPersonal.ShowShortcut = ((bool)(resources.GetObject("MenuItemPersonal.ShowShortcut")));
			this.MenuItemPersonal.Text = resources.GetString("MenuItemPersonal.Text");
			this.MenuItemPersonal.Visible = ((bool)(resources.GetObject("MenuItemPersonal.Visible")));
			this.MenuItemPersonal.Click += new System.EventHandler(this.MenuItemPersonal_Click);
			// 
			// MenuItemShared
			// 
			this.MenuItemShared.Enabled = ((bool)(resources.GetObject("MenuItemShared.Enabled")));
			this.MenuItemShared.Index = 1;
			this.MenuItemShared.Shortcut = ((System.Windows.Forms.Shortcut)(resources.GetObject("MenuItemShared.Shortcut")));
			this.MenuItemShared.ShowShortcut = ((bool)(resources.GetObject("MenuItemShared.ShowShortcut")));
			this.MenuItemShared.Text = resources.GetString("MenuItemShared.Text");
			this.MenuItemShared.Visible = ((bool)(resources.GetObject("MenuItemShared.Visible")));
			this.MenuItemShared.Click += new System.EventHandler(this.MenuItemShared_Click);
			// 
			// MainMenuItemHelp
			// 
			this.MainMenuItemHelp.Enabled = ((bool)(resources.GetObject("MainMenuItemHelp.Enabled")));
			this.MainMenuItemHelp.Index = 4;
			this.MainMenuItemHelp.MenuItems.AddRange(new System.Windows.Forms.MenuItem[] {
																							 this.MenuItemContents,
																							 this.MenuItemAbout});
			this.MainMenuItemHelp.Shortcut = ((System.Windows.Forms.Shortcut)(resources.GetObject("MainMenuItemHelp.Shortcut")));
			this.MainMenuItemHelp.ShowShortcut = ((bool)(resources.GetObject("MainMenuItemHelp.ShowShortcut")));
			this.MainMenuItemHelp.Text = resources.GetString("MainMenuItemHelp.Text");
			this.MainMenuItemHelp.Visible = ((bool)(resources.GetObject("MainMenuItemHelp.Visible")));
			// 
			// MenuItemContents
			// 
			this.MenuItemContents.Enabled = ((bool)(resources.GetObject("MenuItemContents.Enabled")));
			this.MenuItemContents.Index = 0;
			this.MenuItemContents.Shortcut = ((System.Windows.Forms.Shortcut)(resources.GetObject("MenuItemContents.Shortcut")));
			this.MenuItemContents.ShowShortcut = ((bool)(resources.GetObject("MenuItemContents.ShowShortcut")));
			this.MenuItemContents.Text = resources.GetString("MenuItemContents.Text");
			this.MenuItemContents.Visible = ((bool)(resources.GetObject("MenuItemContents.Visible")));
			// 
			// MenuItemAbout
			// 
			this.MenuItemAbout.Enabled = ((bool)(resources.GetObject("MenuItemAbout.Enabled")));
			this.MenuItemAbout.Index = 1;
			this.MenuItemAbout.Shortcut = ((System.Windows.Forms.Shortcut)(resources.GetObject("MenuItemAbout.Shortcut")));
			this.MenuItemAbout.ShowShortcut = ((bool)(resources.GetObject("MenuItemAbout.ShowShortcut")));
			this.MenuItemAbout.Text = resources.GetString("MenuItemAbout.Text");
			this.MenuItemAbout.Visible = ((bool)(resources.GetObject("MenuItemAbout.Visible")));
			// 
			// MainFormTreeView
			// 
			this.MainFormTreeView.AccessibleDescription = resources.GetString("MainFormTreeView.AccessibleDescription");
			this.MainFormTreeView.AccessibleName = resources.GetString("MainFormTreeView.AccessibleName");
			this.MainFormTreeView.Anchor = ((System.Windows.Forms.AnchorStyles)(resources.GetObject("MainFormTreeView.Anchor")));
			this.MainFormTreeView.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("MainFormTreeView.BackgroundImage")));
			this.MainFormTreeView.ContextMenu = this.TreeViewContextMenu;
			this.MainFormTreeView.Dock = ((System.Windows.Forms.DockStyle)(resources.GetObject("MainFormTreeView.Dock")));
			this.MainFormTreeView.Enabled = ((bool)(resources.GetObject("MainFormTreeView.Enabled")));
			this.MainFormTreeView.Font = ((System.Drawing.Font)(resources.GetObject("MainFormTreeView.Font")));
			this.MainFormTreeView.ImageIndex = ((int)(resources.GetObject("MainFormTreeView.ImageIndex")));
			this.MainFormTreeView.ImageList = this.ImageListTreeIcons;
			this.MainFormTreeView.ImeMode = ((System.Windows.Forms.ImeMode)(resources.GetObject("MainFormTreeView.ImeMode")));
			this.MainFormTreeView.Indent = ((int)(resources.GetObject("MainFormTreeView.Indent")));
			this.MainFormTreeView.ItemHeight = ((int)(resources.GetObject("MainFormTreeView.ItemHeight")));
			this.MainFormTreeView.Location = ((System.Drawing.Point)(resources.GetObject("MainFormTreeView.Location")));
			this.MainFormTreeView.Name = "MainFormTreeView";
			this.MainFormTreeView.RightToLeft = ((System.Windows.Forms.RightToLeft)(resources.GetObject("MainFormTreeView.RightToLeft")));
			this.MainFormTreeView.SelectedImageIndex = ((int)(resources.GetObject("MainFormTreeView.SelectedImageIndex")));
			this.MainFormTreeView.ShowLines = false;
			this.MainFormTreeView.ShowRootLines = false;
			this.MainFormTreeView.Size = ((System.Drawing.Size)(resources.GetObject("MainFormTreeView.Size")));
			this.MainFormTreeView.TabIndex = ((int)(resources.GetObject("MainFormTreeView.TabIndex")));
			this.MainFormTreeView.Text = resources.GetString("MainFormTreeView.Text");
			this.MainFormTreeView.Visible = ((bool)(resources.GetObject("MainFormTreeView.Visible")));
			this.MainFormTreeView.MouseDown += new System.Windows.Forms.MouseEventHandler(this.MainFormTreeView_MouseDown);
			this.MainFormTreeView.DoubleClick += new System.EventHandler(this.MainFormTreeView_DoubleClick);
			this.MainFormTreeView.AfterSelect += new System.Windows.Forms.TreeViewEventHandler(this.MainFormTreeView_AfterSelect);
			this.MainFormTreeView.AfterLabelEdit += new System.Windows.Forms.NodeLabelEditEventHandler(this.MainFormTreeView_AfterLabelEdit);
			// 
			// TreeViewContextMenu
			// 
			this.TreeViewContextMenu.RightToLeft = ((System.Windows.Forms.RightToLeft)(resources.GetObject("TreeViewContextMenu.RightToLeft")));
			this.TreeViewContextMenu.Popup += new System.EventHandler(this.TreeViewContextMenu_Popup);
			// 
			// ImageListTreeIcons
			// 
			this.ImageListTreeIcons.ColorDepth = System.Windows.Forms.ColorDepth.Depth32Bit;
			this.ImageListTreeIcons.ImageSize = ((System.Drawing.Size)(resources.GetObject("ImageListTreeIcons.ImageSize")));
			this.ImageListTreeIcons.TransparentColor = System.Drawing.Color.Transparent;
			// 
			// MainFormSplitter
			// 
			this.MainFormSplitter.AccessibleDescription = resources.GetString("MainFormSplitter.AccessibleDescription");
			this.MainFormSplitter.AccessibleName = resources.GetString("MainFormSplitter.AccessibleName");
			this.MainFormSplitter.Anchor = ((System.Windows.Forms.AnchorStyles)(resources.GetObject("MainFormSplitter.Anchor")));
			this.MainFormSplitter.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("MainFormSplitter.BackgroundImage")));
			this.MainFormSplitter.Dock = ((System.Windows.Forms.DockStyle)(resources.GetObject("MainFormSplitter.Dock")));
			this.MainFormSplitter.Enabled = ((bool)(resources.GetObject("MainFormSplitter.Enabled")));
			this.MainFormSplitter.Font = ((System.Drawing.Font)(resources.GetObject("MainFormSplitter.Font")));
			this.MainFormSplitter.ImeMode = ((System.Windows.Forms.ImeMode)(resources.GetObject("MainFormSplitter.ImeMode")));
			this.MainFormSplitter.Location = ((System.Drawing.Point)(resources.GetObject("MainFormSplitter.Location")));
			this.MainFormSplitter.MinExtra = ((int)(resources.GetObject("MainFormSplitter.MinExtra")));
			this.MainFormSplitter.MinSize = ((int)(resources.GetObject("MainFormSplitter.MinSize")));
			this.MainFormSplitter.Name = "MainFormSplitter";
			this.MainFormSplitter.RightToLeft = ((System.Windows.Forms.RightToLeft)(resources.GetObject("MainFormSplitter.RightToLeft")));
			this.MainFormSplitter.Size = ((System.Drawing.Size)(resources.GetObject("MainFormSplitter.Size")));
			this.MainFormSplitter.TabIndex = ((int)(resources.GetObject("MainFormSplitter.TabIndex")));
			this.MainFormSplitter.TabStop = false;
			this.MainFormSplitter.Visible = ((bool)(resources.GetObject("MainFormSplitter.Visible")));
			// 
			// OpenImageFileDialog
			// 
			this.OpenImageFileDialog.DefaultExt = "jpg";
			this.OpenImageFileDialog.Filter = resources.GetString("OpenImageFileDialog.Filter");
			this.OpenImageFileDialog.Title = resources.GetString("OpenImageFileDialog.Title");
			// 
			// FolderChooserDialog
			// 
			this.FolderChooserDialog.Description = resources.GetString("FolderChooserDialog.Description");
			this.FolderChooserDialog.SelectedPath = resources.GetString("FolderChooserDialog.SelectedPath");
			this.FolderChooserDialog.ShowNewFolderButton = false;
			// 
			// MainFrameListView
			// 
			this.MainFrameListView.AccessibleDescription = resources.GetString("MainFrameListView.AccessibleDescription");
			this.MainFrameListView.AccessibleName = resources.GetString("MainFrameListView.AccessibleName");
			this.MainFrameListView.Alignment = ((System.Windows.Forms.ListViewAlignment)(resources.GetObject("MainFrameListView.Alignment")));
			this.MainFrameListView.Anchor = ((System.Windows.Forms.AnchorStyles)(resources.GetObject("MainFrameListView.Anchor")));
			this.MainFrameListView.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("MainFrameListView.BackgroundImage")));
			this.MainFrameListView.Dock = ((System.Windows.Forms.DockStyle)(resources.GetObject("MainFrameListView.Dock")));
			this.MainFrameListView.Enabled = ((bool)(resources.GetObject("MainFrameListView.Enabled")));
			this.MainFrameListView.Font = ((System.Drawing.Font)(resources.GetObject("MainFrameListView.Font")));
			this.MainFrameListView.ImeMode = ((System.Windows.Forms.ImeMode)(resources.GetObject("MainFrameListView.ImeMode")));
			this.MainFrameListView.LabelWrap = ((bool)(resources.GetObject("MainFrameListView.LabelWrap")));
			this.MainFrameListView.LargeImageList = this.ListViewImageList;
			this.MainFrameListView.Location = ((System.Drawing.Point)(resources.GetObject("MainFrameListView.Location")));
			this.MainFrameListView.Name = "MainFrameListView";
			this.MainFrameListView.RightToLeft = ((System.Windows.Forms.RightToLeft)(resources.GetObject("MainFrameListView.RightToLeft")));
			this.MainFrameListView.Size = ((System.Drawing.Size)(resources.GetObject("MainFrameListView.Size")));
			this.MainFrameListView.Sorting = System.Windows.Forms.SortOrder.Ascending;
			this.MainFrameListView.TabIndex = ((int)(resources.GetObject("MainFrameListView.TabIndex")));
			this.MainFrameListView.Text = resources.GetString("MainFrameListView.Text");
			this.MainFrameListView.Visible = ((bool)(resources.GetObject("MainFrameListView.Visible")));
			this.MainFrameListView.DoubleClick += new System.EventHandler(this.MainFormListView_DoubleClick);
			// 
			// ListViewImageList
			// 
			this.ListViewImageList.ColorDepth = System.Windows.Forms.ColorDepth.Depth32Bit;
			this.ListViewImageList.ImageSize = ((System.Drawing.Size)(resources.GetObject("ListViewImageList.ImageSize")));
			this.ListViewImageList.TransparentColor = System.Drawing.Color.Transparent;
			// 
			// PhotoMain
			// 
			this.AccessibleDescription = resources.GetString("$this.AccessibleDescription");
			this.AccessibleName = resources.GetString("$this.AccessibleName");
			this.AutoScaleBaseSize = ((System.Drawing.Size)(resources.GetObject("$this.AutoScaleBaseSize")));
			this.AutoScroll = ((bool)(resources.GetObject("$this.AutoScroll")));
			this.AutoScrollMargin = ((System.Drawing.Size)(resources.GetObject("$this.AutoScrollMargin")));
			this.AutoScrollMinSize = ((System.Drawing.Size)(resources.GetObject("$this.AutoScrollMinSize")));
			this.BackColor = System.Drawing.SystemColors.Control;
			this.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("$this.BackgroundImage")));
			this.ClientSize = ((System.Drawing.Size)(resources.GetObject("$this.ClientSize")));
			this.Controls.Add(this.MainFrameListView);
			this.Controls.Add(this.MainFormSplitter);
			this.Controls.Add(this.MainFormTreeView);
			this.Enabled = ((bool)(resources.GetObject("$this.Enabled")));
			this.Font = ((System.Drawing.Font)(resources.GetObject("$this.Font")));
			this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
			this.ImeMode = ((System.Windows.Forms.ImeMode)(resources.GetObject("$this.ImeMode")));
			this.Location = ((System.Drawing.Point)(resources.GetObject("$this.Location")));
			this.MaximumSize = ((System.Drawing.Size)(resources.GetObject("$this.MaximumSize")));
			this.Menu = this.MainFormMenu;
			this.MinimumSize = ((System.Drawing.Size)(resources.GetObject("$this.MinimumSize")));
			this.Name = "PhotoMain";
			this.RightToLeft = ((System.Windows.Forms.RightToLeft)(resources.GetObject("$this.RightToLeft")));
			this.StartPosition = ((System.Windows.Forms.FormStartPosition)(resources.GetObject("$this.StartPosition")));
			this.Text = resources.GetString("$this.Text");
			this.MouseDown += new System.Windows.Forms.MouseEventHandler(this.MainFormTreeView_MouseDown);
			this.ResumeLayout(false);

		}
		#endregion

		/// <summary>
		/// The main entry point for the application.
		/// </summary>
		[STAThread]
		static void Main() 
		{
			Application.Run(new PhotoMain());
		}

		#region Main Menu stuff
		private void MenuItemExit_Click(object sender, System.EventArgs e)
		{
			this.Dispose(true);
		}

		private void MenuItemLogin_Click(object sender, System.EventArgs e)
		{
			LoginDialog dialog = new LoginDialog();
			if (dialog.ShowDialog() == DialogResult.OK) 
			{
				if (currentUser == null) {
					currentUser = new User();
				}
				try 
				{
					currentUser.performLogin(dialog.Loginame, dialog.Password);

					// set the root element of the tree
					Category rootCat = currentUser.RootCategory;
					TreeNode root = new TreeNode(rootCat.Name);
					root.Tag = rootCat;
					root.ImageIndex = 0;
					MainFormTreeView.Nodes.Add(root);
					MainFormTreeView.Refresh();
					
					enableMenu(true);
					dialog.Dispose();
				} 
				catch (ServerInfoException ex) 
				{
					MessageBox.Show(this, ex.Message, "Info", 
						MessageBoxButtons.OK, MessageBoxIcon.Information);
					dialog.Dispose();
					// try again
					MenuItemLogin_Click(sender, e);
				}
				catch (ObjectDBException ex) 
				{
					MessageBox.Show(this, ex.Message, "Error", 
						MessageBoxButtons.OK, MessageBoxIcon.Error);
					dialog.Dispose();
				}
				dialog = null;
				GC.Collect();
			}
		}

		private void MenuItemLogout_Click(object sender, System.EventArgs e)
		{
			if (MenuItemLogout.Enabled == true && currentUser != null) 
			{
				try 
				{
					currentUser.performLogout();
					currentUser.Dispose();
					currentUser = null;
					MainFormTreeView.Nodes.Clear();
					ListViewImageList.Images.Clear();
					MainFrameListView.Items.Clear();
					enableMenu(false);
					GC.Collect();
				} 
				catch (ServerInfoException ex) 
				{
					MessageBox.Show(this, ex.Message, "Info", 
						MessageBoxButtons.OK, MessageBoxIcon.Information);
				}
				catch (ObjectDBException ex) 
				{
					MessageBox.Show(this, ex.Message + 
						"/n     Application will terminate!", "Error", 
						MessageBoxButtons.OK, MessageBoxIcon.Error);
					this.Dispose();
					currentUser = null;
				} 
			}
		}

		private void MenuItemNewUser_Click(object sender, System.EventArgs e)
		{
			NewUserDialog dialog = new NewUserDialog();
			if (dialog.ShowDialog() == DialogResult.OK) 
			{
				currentUser = new User();
				try 
				{
					currentUser.Alias = dialog.Alias;
					currentUser.Name = dialog.UserName;
					currentUser.Surname = dialog.UserSurname;
					currentUser.Password = dialog.Password;
					currentUser.save();

					// set the root element of the tree
					Category rootCat = currentUser.RootCategory;
					TreeNode root = new TreeNode(rootCat.Name);
					root.Tag = rootCat;
					root.ImageIndex = 0;
					MainFormTreeView.Nodes.Add(root);
					
					enableMenu(true);
					dialog.Dispose();
				} 
				catch (ServerInfoException ex) 
				{
					MessageBox.Show(dialog, ex.Message, "Info", 
						MessageBoxButtons.OK, MessageBoxIcon.Information);
					dialog.Dispose();
					// try again
					MenuItemNewUser_Click(sender, e);
				}
				catch (ObjectDBException ex) 
				{
					MessageBox.Show(this, ex.Message + "\n" + 
						"Application will terminate.", "Error", 
						MessageBoxButtons.OK, MessageBoxIcon.Error);
					dialog.Dispose();
				} 
			}
		}

		private void MenuItemDeleteUser_Click(object sender, System.EventArgs e)
		{
			if (MenuItemDeleteUser.Enabled == true && currentUser != null) 
			{
				try 
				{
					bool confirmed = MessageBox.Show(
						"This operation will delete all the data associated with this user.\n"+ 
						"\tDo you want to continue?",	
						"Confirmation Dialog",	MessageBoxButtons.YesNo, 
						MessageBoxIcon.Warning) == DialogResult.Yes;
					if (confirmed) 
					{
						currentUser.delete();
						currentUser.Dispose();
						currentUser = null;
						MainFormTreeView.Nodes.Clear();
						MainFrameListView.Items.Clear();
						ListViewImageList.Images.Clear();
						enableMenu(false);
						GC.Collect();
					}
				} 
				catch (ServerInfoException ex) 
				{
					MessageBox.Show(this, ex.Message, "Info", 
						MessageBoxButtons.OK, MessageBoxIcon.Information);
				}
				catch (ObjectDBException ex) 
				{
					MessageBox.Show(this, ex.Message + "\n" + 
						"Application will terminate.", "Error", 
						MessageBoxButtons.OK, MessageBoxIcon.Error);
					Dispose(true);
				} 
			}
		}

		private void MenuItemChangePassword_Click(object sender, System.EventArgs e)
		{
			ChangePasswordDialog dialog = new ChangePasswordDialog();
			dialog.Alias = currentUser.Alias;
			if (dialog.ShowDialog() == DialogResult.OK && currentUser != null) 
			{
				try 
				{
					if (currentUser.checkUser(dialog.Alias, dialog.Password)) 
					{
						currentUser.Password = dialog.NewPassword;
						MessageBox.Show(this, "Password successfuly changed!", "Info", 
							MessageBoxButtons.OK, MessageBoxIcon.Information);
					} 
					dialog.Dispose();
				} 
				catch (ServerInfoException ex) 
				{
					MessageBox.Show(this, ex.Message, "Info", 
						MessageBoxButtons.OK, MessageBoxIcon.Information);
					dialog.Dispose();
					// try again
					MenuItemChangePassword_Click(sender, e);
				}
				catch (ObjectDBException ex) 
				{
					MessageBox.Show(this, ex.Message, "Error", 
						MessageBoxButtons.OK, MessageBoxIcon.Error);
					currentUser.Dispose();
					currentUser = null;
					enableMenu(false);
				} 
			}
		}		
		
		private void MenuItemChangeProfile_Click(object sender, System.EventArgs e)
		{
			ChangeProfileDialog dialog = new ChangeProfileDialog();
			dialog.Alias = currentUser.Alias;
			dialog.UserName = currentUser.Name;
			dialog.UserSurname = currentUser.Surname;
			if (dialog.ShowDialog() == DialogResult.OK && currentUser != null) 
			{
				try 
				{
					if (currentUser.checkUser(dialog.Alias, dialog.Password)) 
					{
						currentUser.Name = dialog.UserName;
						currentUser.Surname = dialog.UserSurname;
						currentUser.update();
						MessageBox.Show(this, "User info successfuly changed!", "Info", 
							MessageBoxButtons.OK, MessageBoxIcon.Information);
					} 
					dialog.Dispose();
				} 
				catch (ServerInfoException ex) 
				{
					MessageBox.Show(this, ex.Message, "Info", 
						MessageBoxButtons.OK, MessageBoxIcon.Information);
					dialog.Dispose();
					// try again
					MenuItemChangeProfile_Click(sender, e);
				}
				catch (ObjectDBException ex) 
				{
					MessageBox.Show(this, ex.Message, "Error", 
						MessageBoxButtons.OK, MessageBoxIcon.Error);
					currentUser.Dispose();
					currentUser = null;
					enableMenu(false);
				} 
			}
		}
		
		private void MenuItemAddFolder_Click(object sender, System.EventArgs e)
		{
			TreeNode selectedNode = MainFormTreeView.SelectedNode;
			if (selectedNode != null && selectedNode.Tag is Category)
			{
				Category item = selectedNode.Tag as Category;
				if (FolderChooserDialog.ShowDialog() == DialogResult.OK && item != null)
				{
					string realFileName = FolderChooserDialog.SelectedPath;
					try 
					{
						// load the directory and all the images inside
						Category newCat = new Category(item, realFileName, false);
						createNewCategoryNode(newCat, selectedNode);						
					} 
					catch (ServerInfoException ex) 
					{
						MessageBox.Show(this, ex.Message, "Info", 
							MessageBoxButtons.OK, MessageBoxIcon.Information);
					}
					catch (ObjectDBException ex) 
					{
						string message = ex.Message;
						if (ex.InnerException != null) 
						{
							message += "\n" + ex.InnerException.Message;
						}
						MessageBox.Show(this, message, "Error", 
							MessageBoxButtons.OK, MessageBoxIcon.Error);
					} 
				}
			} 
			else 
			{
				MessageBox.Show(this, "Please select a target categorty!", "Warning", 
					MessageBoxButtons.OK, MessageBoxIcon.Warning);
			}
		}

		private void MenuItemShared_Click(object sender, System.EventArgs e)
		{
			if (MenuItemShared.Enabled == true && currentUser != null) 
			{
				try 
				{
					MainFormTreeView.Nodes.Clear();
					ListViewImageList.Images.Clear();
					MainFrameListView.Items.Clear();

					MainFormTreeView.BeginUpdate();
					ArrayList shared = currentUser.viewSharedDBItems();
					foreach (DBFile item in shared) 
					{
						if (item == null) continue;
						TreeNode root = new TreeNode(item.Name);
						root.Tag = item;
						if (item is Category) 
						{
							root.ImageIndex = 0;
							root.SelectedImageIndex = 0;
						} 
						else if (item is PhotoAlbumDB.Entities.Image)
						{
							root.ImageIndex = 2;
							root.SelectedImageIndex = 2;
						}
						MainFormTreeView.Nodes.Add(root);
					}
					MainFormTreeView.EndUpdate();
				} 
				catch (ServerInfoException ex) 
				{
					MessageBox.Show(this, ex.Message, "Info", 
						MessageBoxButtons.OK, MessageBoxIcon.Information);
				}
				catch (ObjectDBException ex) 
				{
					MessageBox.Show(this, ex.Message, "Error", 
						MessageBoxButtons.OK, MessageBoxIcon.Error);
				} 
			}
		}

		private void MenuItemPersonal_Click(object sender, System.EventArgs e)
		{
			if (MenuItemShared.Enabled == true && currentUser != null) 
			{
				try 
				{
					MainFormTreeView.Nodes.Clear();
					ListViewImageList.Images.Clear();
					MainFrameListView.Items.Clear();

					// set the root element of the tree
					Category rootCat = currentUser.RootCategory;
					TreeNode root = new TreeNode(rootCat.Name);
					root.Tag = rootCat;
					root.ImageIndex = 0;
					MainFormTreeView.Nodes.Add(root);
					MainFormTreeView.Refresh();
				} 
				catch (ServerInfoException ex) 
				{
					MessageBox.Show(this, ex.Message, "Info", 
						MessageBoxButtons.OK, MessageBoxIcon.Information);
				}
				catch (ObjectDBException ex) 
				{
					MessageBox.Show(this, ex.Message, "Error", 
						MessageBoxButtons.OK, MessageBoxIcon.Error);
				} 
			}
		}

		private void enableMenu(bool enable) 
		{
			MenuItemFileOpen.Enabled = enable;
			MenuItemAddFolder.Enabled = enable;
			MenuItemAddFolder.Enabled = enable;
			MenuItemChangePassword.Enabled = enable;
			MenuItemChangeProfile.Enabled = enable;
			MenuItemDeleteUser.Enabled = enable;
			MenuItemPersonal.Enabled = enable;
			MenuItemShared.Enabled = enable;
			MenuItemLogin.Enabled = !enable;
			MenuItemLogout.Enabled = enable;
			MenuItemNewUser.Enabled = !enable;
		}
		
		#endregion

		#region Tree View stuff
		private void MainFormTreeView_AfterSelect(object sender, TreeViewEventArgs e)
		{
			TreeNode node = e.Node;
			if (node != null) 
			{
				if (node.Tag is Category)
				{
					BindTree(node);				
					node.Expand();
				}
				else if (node.Tag is PhotoAlbumDB.Entities.Image)
				{
					viewTag(node);
				}
			} 	
		}

		private void BindTree(TreeNode parent) 
		{
			try 
			{
				MainFormTreeView.BeginUpdate();
				clearSubnodes(parent);
				
				bool containsItems = false;
				Category parentCategory = parent.Tag as Category;
				if (parentCategory != null) 
				{
					IList subItems = parentCategory.SubItems;
					foreach (DBFile item in subItems) 
					{
						if (item == null) continue;
						TreeNode child = new TreeNode(item.Name);
						child.Tag = item;
						if (item is Category) 
						{
							child.ImageIndex = 0;
							child.SelectedImageIndex = 0;
						} 
						else if (item is PhotoAlbumDB.Entities.Image)
						{
							child.ImageIndex = 2;
							child.SelectedImageIndex = 2;
						}
						parent.Nodes.Add(child);
						containsItems = true;
					}
					subItems = null;
				}

				if (parent.Tag is Category && containsItems) 
				{
					parent.ImageIndex = 1;
					parent.SelectedImageIndex = 1;
				} 
				MainFormTreeView.EndUpdate();
			} 
			catch (ServerInfoException ex) 
			{
				MessageBox.Show(this, ex.Message, "Info", 
					MessageBoxButtons.OK, MessageBoxIcon.Information);
			}
			catch (ObjectDBException ex) 
			{
				MessageBox.Show(this, ex.Message, "Error", 
					MessageBoxButtons.OK, MessageBoxIcon.Error);
			} 
		}

		private void clearSubnodes(TreeNode parent) 
		{
			if (parent == null) return;
			foreach (TreeNode child in parent.Nodes) 
			{
				// call dispose on the DBFile contained
				// in the tree node tag
				child.Tag = null;
			}
			parent.Nodes.Clear();
		}


		private void MainFormTreeView_DoubleClick(object sender, System.EventArgs e) 
		{
			TreeNode mySelectedNode = MainFormTreeView.SelectedNode;
			if (mySelectedNode != null && mySelectedNode.Parent != null)
			{
				MainFormTreeView.LabelEdit = true;
				if(!mySelectedNode.IsEditing)
				{
					mySelectedNode.BeginEdit();
				}
			}
		}

		private void MainFormTreeView_AfterLabelEdit(object sender, 
			System.Windows.Forms.NodeLabelEditEventArgs e)
		{
			if (e.Label != null)
			{
				bool hasNewEdit = false;
				if(e.Label.Length > 0)
				{
					DBFile item = e.Node.Tag as DBFile;
					if (item != null) 
					{
						try 
						{
							item.Name = e.Label;
							item.update();
							e.Node.EndEdit(false);
						}
						catch (ServerInfoException ex) 
						{
							e.CancelEdit = true;
							MessageBox.Show(this, ex.Message, "Info", 
								MessageBoxButtons.OK, MessageBoxIcon.Information);
							e.Node.BeginEdit();
							hasNewEdit = true;
						}
						catch (ObjectDBException ex) 
						{
							e.CancelEdit = true;
							MessageBox.Show(this, ex.Message, "Error", 
								MessageBoxButtons.OK, MessageBoxIcon.Error);
							e.Node.BeginEdit();
							hasNewEdit = true;
						} 
					} 
					else {e.CancelEdit = true;}
				}
				else
				{
					/* Cancel the label edit action */
					e.CancelEdit = true;
				}
				if (!hasNewEdit) 
				{
					this.MainFormTreeView.LabelEdit = false;
				}
			}
		}


		private void MainFormTreeView_MouseDown(object sender, 
			System.Windows.Forms.MouseEventArgs e)
		{
			treeMouseLocation = new Point(e.X, e.Y);
		}

		#endregion 

		#region Context Menu Stuff
		private void TreeViewContextMenu_Popup(object sender, System.EventArgs e)
		{
			TreeViewContextMenu.MenuItems.Clear();

			TreeNode selectedNode = MainFormTreeView.GetNodeAt(treeMouseLocation);
			if (selectedNode != null && selectedNode.Tag is DBFile)
			{
				if (selectedNode.Tag is Category) 
				{
					TreeViewContextMenu.MenuItems.Add("New category", 
						new System.EventHandler(this.NewCategory_OnClick));
					MenuItem loadMenu = new MenuItem("Load");
					loadMenu.MenuItems.Add("File", 
						new System.EventHandler(this.LoadFile_OnClick));
					loadMenu.MenuItems.Add("Single Directory", 
						new System.EventHandler(this.LoadSingleDirectory_OnClick));
					loadMenu.MenuItems.Add("Deep Directory", 
						new System.EventHandler(this.LoadDeepDirectory_OnClick));
					TreeViewContextMenu.MenuItems.Add(loadMenu);
					TreeViewContextMenu.MenuItems.Add("-");
				}

				TreeViewContextMenu.MenuItems.Add("View", 
					new System.EventHandler(this.View_OnClick));
				TreeViewContextMenu.MenuItems.Add("Share", 
					new System.EventHandler(this.Share_OnClick));
				TreeViewContextMenu.MenuItems.Add("Delete", 
					new System.EventHandler(this.Delete_OnClick));
				TreeViewContextMenu.MenuItems.Add("-");
				TreeViewContextMenu.MenuItems.Add("Properties", 
					new System.EventHandler(this.Properties_OnClick));
			}
		}

		private void Properties_OnClick(System.Object sender, System.EventArgs e)
		{
			TreeNode selectedNode = MainFormTreeView.GetNodeAt(treeMouseLocation);
			if (selectedNode != null && selectedNode.Tag is DBFile)
			{
				DBFile item = selectedNode.Tag as DBFile;
				PropertiesDialog dialog = new PropertiesDialog();
				try 
				{
					// fill the properties dialog first
					dialog.ItemShared = item.Shared;
					dialog.ItemName = item.Name;
					dialog.ItemOwner = item.Owner;
					dialog.ItemCreated = item.DateModified.ToString();
					dialog.ItemSize = item.Size == 0 ? 
						"0 bytes" : item.Size.ToString("## ### ### ### ### bytes");
					if (item is PhotoAlbumDB.Entities.Image && item != null)
					{
						PhotoAlbumDB.Entities.Image img = 
							item as PhotoAlbumDB.Entities.Image;
						dialog.ItemDescription = img.Description;
					}

					if (dialog.ShowDialog() == DialogResult.OK && 
						hasChanges(dialog, item)) 
					{
						item.Name = dialog.ItemName;
						if (item is PhotoAlbumDB.Entities.Image && item != null)
						{
							PhotoAlbumDB.Entities.Image img = 
								item as PhotoAlbumDB.Entities.Image;
							img.Description = dialog.ItemDescription;
						}
						item.update();
						selectedNode.Text = item.Name;
					}
					dialog.Dispose();
				} 
				catch (ServerInfoException ex) 
				{
					MessageBox.Show(dialog, ex.Message, "Info", 
						MessageBoxButtons.OK, MessageBoxIcon.Information);
					dialog.Dispose();
				}
				catch (ObjectDBException ex) 
				{
					MessageBox.Show(this, ex.Message, "Error", 
						MessageBoxButtons.OK, MessageBoxIcon.Error);
					dialog.Dispose();
				} 
			}			
		}

		private bool hasChanges(PropertiesDialog dialog, DBFile item) 
		{
			if (!dialog.ItemName.Equals(item.Name))  return true;
			if (item is PhotoAlbumDB.Entities.Image && item != null) 
			{
				PhotoAlbumDB.Entities.Image img = 
					item as PhotoAlbumDB.Entities.Image;
				if (!dialog.ItemDescription.Equals(img.Description)) 
				{
					return true;
				}
			}
			return false;
		}

		private void NewCategory_OnClick(System.Object sender, System.EventArgs e)
		{
			TreeNode selectedNode = MainFormTreeView.GetNodeAt(treeMouseLocation);
			if (selectedNode != null && selectedNode.Tag is Category)
			{
				Category parent = selectedNode.Tag as Category;
				try 
				{
					Category newCat = new Category(parent);
					newCat.Name = "New Category";
					newCat.save();
					createNewCategoryNode(newCat, selectedNode);
				} 
				catch (ServerInfoException ex) 
				{
					MessageBox.Show(this, ex.Message, "Info", 
						MessageBoxButtons.OK, MessageBoxIcon.Information);
				}
				catch (ObjectDBException ex) 
				{
					MessageBox.Show(this, ex.Message, "Error", 
						MessageBoxButtons.OK, MessageBoxIcon.Error);
				} 
			}			
		}

		private void Delete_OnClick(System.Object sender, System.EventArgs e)
		{
			TreeNode selectedNode = MainFormTreeView.GetNodeAt(treeMouseLocation);
			if (selectedNode != null && selectedNode.Tag is DBFile)
			{
				String warningMessage = "This operation will delete this item and all the " +
					"data associated with it.\n"+ 
					"\t   Do you want to continue?";

				bool confirmed = MessageBox.Show(this, warningMessage,	
					"Confirmation Dialog",	MessageBoxButtons.YesNo, 
					MessageBoxIcon.Warning) == DialogResult.Yes;
				if (confirmed) 
				{
					DBFile item = selectedNode.Tag as DBFile;
					try 
					{
						item.delete();
						try 
						{
							selectedNode.Remove();
						} 
						catch (NullReferenceException) 
						{
							// ignore this exception - TreeNode bug probably!
						}
						MainFormTreeView.Refresh();
					} 
					catch (ServerInfoException ex) 
					{
						MessageBox.Show(this, ex.Message, "Info", 
							MessageBoxButtons.OK, MessageBoxIcon.Information);
					}
					catch (ObjectDBException ex) 
					{
						MessageBox.Show(this, ex.Message, "Error", 
							MessageBoxButtons.OK, MessageBoxIcon.Error);
					} 
				}
			}			
		}

		private void Share_OnClick(System.Object sender, System.EventArgs e)
		{
			TreeNode selectedNode = MainFormTreeView.GetNodeAt(treeMouseLocation);
			if (selectedNode != null && selectedNode.Tag is DBFile)
			{
				DBFile item = selectedNode.Tag as DBFile;
				try 
				{
					item.Shared = true;
				} 
				catch (ServerInfoException ex) 
				{
					MessageBox.Show(this, ex.Message, "Info", 
						MessageBoxButtons.OK, MessageBoxIcon.Information);
				}
				catch (ObjectDBException ex) 
				{
					MessageBox.Show(this, ex.Message, "Error", 
						MessageBoxButtons.OK, MessageBoxIcon.Error);
				} 
			}			
		}

		private void LoadFile_OnClick(System.Object sender, System.EventArgs e)
		{
			TreeNode selectedNode = MainFormTreeView.GetNodeAt(treeMouseLocation);
			if (selectedNode != null && selectedNode.Tag is Category)
			{
				Category item = selectedNode.Tag as Category;
				if (OpenImageFileDialog.ShowDialog() == DialogResult.OK && item != null)
				{
					string realFileName = OpenImageFileDialog.FileName;
					try 
					{
						PhotoAlbumDB.Entities.Image newImg = 
							new PhotoAlbumDB.Entities.Image(realFileName, item);
						newImg.save();
						
						TreeNode newImgNode = new TreeNode(newImg.Name);
						newImgNode.Tag = newImg;
						newImgNode.ImageIndex = 2;
						newImgNode.SelectedImageIndex = 2;
						selectedNode.Nodes.Add(newImgNode);
						newImgNode.EnsureVisible();
					} 
					catch (ServerInfoException ex) 
					{
						MessageBox.Show(this, ex.Message, "Info", 
							MessageBoxButtons.OK, MessageBoxIcon.Information);
					}
					catch (ObjectDBException ex) 
					{
						MessageBox.Show(this, ex.Message, "Error", 
							MessageBoxButtons.OK, MessageBoxIcon.Error);
					} 
				}
			}			
		}

		private void LoadSingleDirectory_OnClick(System.Object sender, System.EventArgs e)
		{
			loadDirectory(false);
		}

		private void LoadDeepDirectory_OnClick(System.Object sender, System.EventArgs e)
		{
			loadDirectory(true);
		}

		private void View_OnClick(System.Object sender, System.EventArgs e)
		{
			TreeNode selectedNode = MainFormTreeView.GetNodeAt(treeMouseLocation);
			if (selectedNode != null) 
			{
				viewTag(selectedNode);
			} 				
		}

		private void loadDirectory(bool deep) 
		{
			TreeNode selectedNode = MainFormTreeView.GetNodeAt(treeMouseLocation);
			if (selectedNode != null && selectedNode.Tag is Category)
			{
				Category item = selectedNode.Tag as Category;
				if (FolderChooserDialog.ShowDialog() == DialogResult.OK && item != null)
				{
					string fileName = FolderChooserDialog.SelectedPath;
					try 
					{
						// load all the images under this folder
						mFileLoaderThread = new Thread(
							new ThreadStart(item.loadImagesFromFolder));
						LoadingDialog dialog = new LoadingDialog(mFileLoaderThread);
						item.prepareForLoad(fileName, deep, dialog);
						dialog.startLoading();
						dialog.Show();

						selectedNode.Parent.Collapse();
						selectedNode.EnsureVisible();
					} 
					catch (ServerInfoException ex) 
					{
						MessageBox.Show(this, ex.Message, "Info", 
							MessageBoxButtons.OK, MessageBoxIcon.Information);
					}
					catch (ObjectDBException ex) 
					{
						string message = ex.Message;
						if (ex.InnerException != null) 
						{
							message += "\n" + ex.InnerException.Message;
						}
						MessageBox.Show(this, message, "Error", 
							MessageBoxButtons.OK, MessageBoxIcon.Error);
					} 
				}
			}
		}
			
		private void createNewCategoryNode(Category newCat, TreeNode parentNode) 
		{
			MainFormTreeView.BeginUpdate();
			TreeNode newNode = new TreeNode(newCat.Name);
			newNode.Tag = newCat;
			newNode.ImageIndex = 0;
			newNode.SelectedImageIndex = 0;

			parentNode.Nodes.Add(newNode);
			MainFormTreeView.EndUpdate();
					
			newNode.EnsureVisible();
			MainFormTreeView.LabelEdit = true;
			newNode.BeginEdit();
		}
		#endregion

		private void MainFormListView_DoubleClick(object sender, 
			System.EventArgs e)
		{
			foreach(ListViewItem item in MainFrameListView.SelectedItems) 
			{
				PhotoAlbumDB.Entities.Image image = 
					item.Tag as PhotoAlbumDB.Entities.Image;
				if (image != null) 
				{
					// TODO: Add code for exceptions in the director of the builder
					try {
						image.view(this, false);
					} 
					catch (ServerInfoException ex) 
					{
						MessageBox.Show(this, ex.Message, "Info", 
							MessageBoxButtons.OK, MessageBoxIcon.Information);
					}
					catch (ObjectDBException ex) 
					{
						string message = ex.Message;
						if (ex.InnerException != null) 
						{
							message += "\n" + ex.InnerException.Message;
						}
						MessageBox.Show(this, message, "Error", 
							MessageBoxButtons.OK, MessageBoxIcon.Error);
					} 
					catch (System.ArgumentException) 
					{
						MessageBox.Show(this, "Invalid image file!", "Error", 
							MessageBoxButtons.OK, MessageBoxIcon.Error);
					}
					catch (Exception ex) 
					{
						MessageBox.Show(this, ex.Message, "Error", 
							MessageBoxButtons.OK, MessageBoxIcon.Error);
					}
				}
				break;
			}
		}


		private void viewTag(TreeNode selectedNode) 
		{
			DBFile item = selectedNode.Tag as DBFile;
			if (item != null)
			{
				try 
				{
					// clear all previouse images from the list view
					ListViewImageList.Images.Clear();
					MainFrameListView.Items.Clear();
					item.view(this, true);		
				} 
				catch (ServerInfoException ex) 
				{
					MessageBox.Show(this, ex.Message, "Info", 
						MessageBoxButtons.OK, MessageBoxIcon.Information);
				}
				catch (ObjectDBException ex) 
				{
					string message = ex.Message;
					if (ex.InnerException != null) 
					{
						message += "\n" + ex.InnerException.Message;
					}
					MessageBox.Show(this, message, "Error", 
						MessageBoxButtons.OK, MessageBoxIcon.Error);
				} 
				catch (System.ArgumentException) 
				{
					MessageBox.Show(this, "Error reading image file!", "Error", 
						MessageBoxButtons.OK, MessageBoxIcon.Error);
				}
				catch (Exception ex) 
				{
					MessageBox.Show(this, ex.Message, "Error", 
						MessageBoxButtons.OK, MessageBoxIcon.Error);
				}
			}
		}
	}
}
