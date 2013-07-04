IF EXISTS (SELECT name FROM master.dbo.sysdatabases WHERE name = N'photo')
	DROP DATABASE [photo]
GO

CREATE DATABASE [photo]  ON (NAME = N'photo_Data', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL\data\photo_Data.MDF' , SIZE = 25, FILEGROWTH = 20%) LOG ON (NAME = N'photo_Log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL\data\photo_Log.LDF' , SIZE = 15, FILEGROWTH = 10%)
 COLLATE Cyrillic_General_CI_AS
GO

exec sp_dboption N'photo', N'autoclose', N'false'
GO

exec sp_dboption N'photo', N'bulkcopy', N'false'
GO

exec sp_dboption N'photo', N'trunc. log', N'false'
GO

exec sp_dboption N'photo', N'torn page detection', N'true'
GO

exec sp_dboption N'photo', N'read only', N'false'
GO

exec sp_dboption N'photo', N'dbo use', N'false'
GO

exec sp_dboption N'photo', N'single', N'false'
GO

exec sp_dboption N'photo', N'autoshrink', N'false'
GO

exec sp_dboption N'photo', N'ANSI null default', N'false'
GO

exec sp_dboption N'photo', N'recursive triggers', N'false'
GO

exec sp_dboption N'photo', N'ANSI nulls', N'false'
GO

exec sp_dboption N'photo', N'concat null yields null', N'false'
GO

exec sp_dboption N'photo', N'cursor close on commit', N'false'
GO

exec sp_dboption N'photo', N'default to local cursor', N'false'
GO

exec sp_dboption N'photo', N'quoted identifier', N'false'
GO

exec sp_dboption N'photo', N'ANSI warnings', N'false'
GO

exec sp_dboption N'photo', N'auto create statistics', N'true'
GO

exec sp_dboption N'photo', N'auto update statistics', N'true'
GO

use [photo]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[FK_images_categories]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [dbo].[images] DROP CONSTRAINT FK_images_categories
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[FK_images_formats]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [dbo].[images] DROP CONSTRAINT FK_images_formats
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[FK_categories_user_profiles]') and OBJECTPROPERTY(id, N'IsForeignKey') = 1)
ALTER TABLE [dbo].[categories] DROP CONSTRAINT FK_categories_user_profiles
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_ChangeCategoryName]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_ChangeCategoryName]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_ChangeImageInfo]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_ChangeImageInfo]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_ChangeUserInfo]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_ChangeUserInfo]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_ChangeUserPassword]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_ChangeUserPassword]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_CreateNewCategory]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_CreateNewCategory]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_CreateNewFormat]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_CreateNewFormat]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_CreateNewImage]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_CreateNewImage]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_CreateNewUser]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_CreateNewUser]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_DeleteCategory]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_DeleteCategory]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_DeleteFormat]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_DeleteFormat]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_DeleteImage]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_DeleteImage]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_DeleteUserById]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_DeleteUserById]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_DeleteUserByPass]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_DeleteUserByPass]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_GetAllSubcategoryIds]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_GetAllSubcategoryIds]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_GetCategory]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_GetCategory]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_GetImage]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_GetImage]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_GetImageIdsInCategory]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_GetImageIdsInCategory]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_GetImageInfo]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_GetImageInfo]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_GetImagesInCategory]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_GetImagesInCategory]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_GetRootCategoryId]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_GetRootCategoryId]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_GetSizeOfCategory]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_GetSizeOfCategory]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_GetSubcategories]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_GetSubcategories]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_GetSubcategoryIds]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_GetSubcategoryIds]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_GetUser]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_GetUser]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_HasUserAccessToCategory]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_HasUserAccessToCategory]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_HasUserAccessToImage]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_HasUserAccessToImage]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_PerformLogin]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_PerformLogin]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_PerformLogout]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_PerformLogout]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_ShareCategory]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_ShareCategory]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_ShareImage]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[sp_ShareImage]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[v_AllActiveUsers]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [dbo].[v_AllActiveUsers]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[v_AllSharedImages]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [dbo].[v_AllSharedImages]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[v_AllUsers]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [dbo].[v_AllUsers]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[v_RootSharedCategories]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [dbo].[v_RootSharedCategories]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[v_SharedCategories]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [dbo].[v_SharedCategories]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[v_SinglySharedImages]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [dbo].[v_SinglySharedImages]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[categories]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[categories]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[formats]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[formats]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[images]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[images]
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[user_profiles]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[user_profiles]
GO

if not exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[categories]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
 BEGIN
CREATE TABLE [dbo].[categories] (
	[id] [int] IDENTITY (1, 1) NOT NULL ,
	[category_name] [varchar] (100) COLLATE Cyrillic_General_CI_AS NOT NULL ,
	[date_modified] [datetime] NOT NULL ,
	[parent_id] [int] NULL ,
	[user_id] [int] NOT NULL ,
	[shared] [bit] NOT NULL 
) ON [PRIMARY]
END

GO

if not exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[formats]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
 BEGIN
CREATE TABLE [dbo].[formats] (
	[id] [int] IDENTITY (1, 1) NOT NULL ,
	[format_name] [varchar] (3) COLLATE Cyrillic_General_CI_AS NOT NULL 
) ON [PRIMARY]
END

GO

if not exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[images]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
 BEGIN
CREATE TABLE [dbo].[images] (
	[id] [int] IDENTITY (1, 1) NOT NULL ,
	[file_name] [varchar] (100) COLLATE Cyrillic_General_CI_AS NOT NULL ,
	[image] [image] NULL ,
	[size] [bigint] NOT NULL ,
	[date_modified] [datetime] NOT NULL ,
	[description] [varchar] (500) COLLATE Cyrillic_General_CI_AS NULL ,
	[format_id] [int] NULL ,
	[category_id] [int] NOT NULL ,
	[shared] [bit] NOT NULL 
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
END

GO

if not exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[user_profiles]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
 BEGIN
CREATE TABLE [dbo].[user_profiles] (
	[id] [int] IDENTITY (1, 1) NOT NULL ,
	[alias] [varchar] (50) COLLATE Cyrillic_General_CI_AS NOT NULL ,
	[password] [varchar] (50) COLLATE Cyrillic_General_CI_AS NOT NULL ,
	[user_name] [varchar] (50) COLLATE Cyrillic_General_CI_AS NULL ,
	[user_surname] [varchar] (50) COLLATE Cyrillic_General_CI_AS NULL ,
	[role_id] [int] NULL ,
	[active] [bit] NOT NULL 
) ON [PRIMARY]
END

GO

ALTER TABLE [dbo].[categories] WITH NOCHECK ADD 
	CONSTRAINT [PK_categories] PRIMARY KEY  CLUSTERED 
	(
		[id]
	)  ON [PRIMARY] 
GO

ALTER TABLE [dbo].[formats] WITH NOCHECK ADD 
	CONSTRAINT [PK_formats] PRIMARY KEY  CLUSTERED 
	(
		[id]
	)  ON [PRIMARY] 
GO

ALTER TABLE [dbo].[images] WITH NOCHECK ADD 
	CONSTRAINT [PK_images] PRIMARY KEY  CLUSTERED 
	(
		[id]
	)  ON [PRIMARY] 
GO

ALTER TABLE [dbo].[user_profiles] WITH NOCHECK ADD 
	CONSTRAINT [PK_user_profiles] PRIMARY KEY  CLUSTERED 
	(
		[id]
	)  ON [PRIMARY] 
GO

ALTER TABLE [dbo].[categories] WITH NOCHECK ADD 
	CONSTRAINT [DF_categories_parent_id] DEFAULT (null) FOR [parent_id],
	CONSTRAINT [DF_categories_shared] DEFAULT (0) FOR [shared]
GO

ALTER TABLE [dbo].[images] WITH NOCHECK ADD 
	CONSTRAINT [DF_images_size] DEFAULT (0) FOR [size],
	CONSTRAINT [DF_images_shared] DEFAULT (0) FOR [shared]
GO

ALTER TABLE [dbo].[user_profiles] WITH NOCHECK ADD 
	CONSTRAINT [DF_user_profiles_role_id] DEFAULT (1) FOR [role_id],
	CONSTRAINT [DF_user_profiles_active] DEFAULT (0) FOR [active],
	CONSTRAINT [IX_user_profiles] UNIQUE  NONCLUSTERED 
	(
		[alias],
		[password]
	)  ON [PRIMARY] 
GO

 CREATE  INDEX [IX_categories] ON [dbo].[categories]([parent_id]) ON [PRIMARY]
GO

 CREATE  INDEX [IX_images] ON [dbo].[images]([category_id]) ON [PRIMARY]
GO

ALTER TABLE [dbo].[categories] ADD 
	CONSTRAINT [FK_categories_user_profiles] FOREIGN KEY 
	(
		[user_id]
	) REFERENCES [dbo].[user_profiles] (
		[id]
	) ON DELETE CASCADE 
GO

ALTER TABLE [dbo].[images] ADD 
	CONSTRAINT [FK_images_categories] FOREIGN KEY 
	(
		[category_id]
	) REFERENCES [dbo].[categories] (
		[id]
	) ON DELETE CASCADE ,
	CONSTRAINT [FK_images_formats] FOREIGN KEY 
	(
		[format_id]
	) REFERENCES [dbo].[formats] (
		[id]
	)
GO

alter table [dbo].[images] nocheck constraint [FK_images_formats]
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE VIEW dbo.v_AllActiveUsers
AS
SELECT     TOP 100 PERCENT id, alias, user_name, user_surname
FROM         dbo.user_profiles
WHERE     (active = 1)
ORDER BY alias

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO


exec sp_addextendedproperty N'MS_DiagramPane1', N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1[50] 2[25] 3) )"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1 [56] 4 [18] 2))"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "user_profiles"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 169
               Right = 190
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      RowHeights = 220
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
', N'user', N'dbo', N'view', N'v_AllActiveUsers'
GO
exec sp_addextendedproperty N'MS_DiagramPaneCount', 1, N'user', N'dbo', N'view', N'v_AllActiveUsers'

GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE VIEW dbo.v_AllSharedImages
AS
SELECT     TOP 100 PERCENT dbo.images.id AS image_id, dbo.images.file_name AS image_name, dbo.images.size AS size, 
                      dbo.images.date_modified AS date_modified, dbo.images.description AS description, dbo.formats.format_name AS format, 
                      dbo.user_profiles.alias AS owner, dbo.user_profiles.user_name AS owner_name, dbo.user_profiles.user_surname AS owner_surname
FROM         dbo.categories INNER JOIN
                      dbo.images ON dbo.categories.id = dbo.images.category_id INNER JOIN
                      dbo.formats ON dbo.images.format_id = dbo.formats.id INNER JOIN
                      dbo.user_profiles ON dbo.categories.user_id = dbo.user_profiles.id
WHERE     (dbo.images.shared = 1)
ORDER BY dbo.images.date_modified DESC, dbo.user_profiles.alias, dbo.images.size

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO


exec sp_addextendedproperty N'MS_DiagramPane1', N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1[50] 2[25] 3) )"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1 [56] 4 [18] 2))"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "categories"
            Begin Extent = 
               Top = 0
               Left = 193
               Bottom = 147
               Right = 349
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "formats"
            Begin Extent = 
               Top = 0
               Left = 559
               Bottom = 85
               Right = 711
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "images"
            Begin Extent = 
               Top = 3
               Left = 381
               Bottom = 196
               Right = 533
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "user_profiles"
            Begin Extent = 
               Top = 0
               Left = 0
               Bottom = 159
               Right = 152
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      RowHeights = 220
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 1560
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
', N'user', N'dbo', N'view', N'v_AllSharedImages'
GO
exec sp_addextendedproperty N'MS_DiagramPaneCount', 1, N'user', N'dbo', N'view', N'v_AllSharedImages'

GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE VIEW dbo.v_AllUsers
AS
SELECT     TOP 100 PERCENT id, alias, user_name, user_surname
FROM         dbo.user_profiles
ORDER BY alias

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO


exec sp_addextendedproperty N'MS_DiagramPane1', N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1[50] 2[25] 3) )"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1 [56] 4 [18] 2))"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "user_profiles"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 169
               Right = 190
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      RowHeights = 220
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
', N'user', N'dbo', N'view', N'v_AllUsers'
GO
exec sp_addextendedproperty N'MS_DiagramPaneCount', 1, N'user', N'dbo', N'view', N'v_AllUsers'

GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE VIEW dbo.v_RootSharedCategories
AS
SELECT     TOP 100 PERCENT dbo.categories.id AS category_id
FROM         dbo.categories INNER JOIN
                      dbo.user_profiles ON dbo.categories.user_id = dbo.user_profiles.id INNER JOIN
                      dbo.categories categories_1 ON dbo.categories.parent_id = categories_1.id
WHERE     (dbo.categories.shared = 1) AND (categories_1.shared = 0)
ORDER BY dbo.categories.date_modified DESC, dbo.user_profiles.alias, dbo.categories.category_name

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO


exec sp_addextendedproperty N'MS_DiagramPane1', N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[41] 4[30] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1[50] 2[25] 3) )"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1 [56] 4 [18] 2))"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "categories"
            Begin Extent = 
               Top = 0
               Left = 270
               Bottom = 149
               Right = 426
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "user_profiles"
            Begin Extent = 
               Top = 2
               Left = 516
               Bottom = 168
               Right = 668
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "categories_1"
            Begin Extent = 
               Top = 0
               Left = 0
               Bottom = 157
               Right = 156
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      RowHeights = 220
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
', N'user', N'dbo', N'view', N'v_RootSharedCategories'
GO
exec sp_addextendedproperty N'MS_DiagramPaneCount', 1, N'user', N'dbo', N'view', N'v_RootSharedCategories'

GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE VIEW dbo.v_SharedCategories
AS
SELECT     TOP 100 PERCENT dbo.categories.id AS category_id, dbo.categories.category_name AS category_name, 
                      dbo.categories.date_modified AS date_modified, dbo.user_profiles.alias AS owner, dbo.user_profiles.user_name AS owner_name, 
                      dbo.user_profiles.user_surname AS owner_surname
FROM         dbo.categories INNER JOIN
                      dbo.user_profiles ON dbo.categories.user_id = dbo.user_profiles.id
WHERE     (dbo.categories.shared = 1)
ORDER BY dbo.categories.date_modified DESC, dbo.user_profiles.alias, dbo.categories.category_name

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO


exec sp_addextendedproperty N'MS_DiagramPane1', N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1[50] 2[25] 3) )"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1 [56] 4 [18] 2))"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "categories"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 162
               Right = 194
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "user_profiles"
            Begin Extent = 
               Top = 6
               Left = 232
               Bottom = 179
               Right = 384
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      RowHeights = 220
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1560
         Alias = 1455
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
', N'user', N'dbo', N'view', N'v_SharedCategories'
GO
exec sp_addextendedproperty N'MS_DiagramPaneCount', 1, N'user', N'dbo', N'view', N'v_SharedCategories'

GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE VIEW dbo.v_SinglySharedImages
AS
SELECT     TOP 100 PERCENT dbo.images.id AS image_id
FROM         dbo.categories INNER JOIN
                      dbo.images ON dbo.categories.id = dbo.images.category_id INNER JOIN
                      dbo.formats ON dbo.images.format_id = dbo.formats.id INNER JOIN
                      dbo.user_profiles ON dbo.categories.user_id = dbo.user_profiles.id
WHERE     (dbo.categories.shared = 0) AND (dbo.images.shared = 1)
ORDER BY dbo.images.date_modified DESC, dbo.user_profiles.alias, dbo.images.size

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO


exec sp_addextendedproperty N'MS_DiagramPane1', N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[37] 4[28] 2[27] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1[50] 2[25] 3) )"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1 [56] 4 [18] 2))"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "categories"
            Begin Extent = 
               Top = 0
               Left = 207
               Bottom = 154
               Right = 363
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "images"
            Begin Extent = 
               Top = 0
               Left = 386
               Bottom = 194
               Right = 538
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "formats"
            Begin Extent = 
               Top = 0
               Left = 559
               Bottom = 85
               Right = 711
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "user_profiles"
            Begin Extent = 
               Top = 0
               Left = 0
               Bottom = 158
               Right = 152
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      RowHeights = 220
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 1455
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
', N'user', N'dbo', N'view', N'v_SinglySharedImages'
GO
exec sp_addextendedproperty N'MS_DiagramPaneCount', 1, N'user', N'dbo', N'view', N'v_SinglySharedImages'

GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE PROCEDURE dbo.sp_ChangeCategoryName
(
	@Category_Id INT,
	@New_Name VARCHAR(50), 
	@User_Id INT
)
AS	
	-- SET TRANSACTION ISOLATION LEVEL SERIALIZABLE
	BEGIN TRANSACTION
	
	-- Check if user has access to this category
	DECLARE @Has_Access BIT
	
	EXECUTE sp_HasUserAccessToCategory @User_Id, 
		@Category_Id, @Has_Access OUTPUT
	
	IF (@Has_Access <> 1) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Access denied!', 17, 1)
		RETURN 1
	END
	
	-- Check if this is a root category	
	IF (SELECT parent_id FROM categories WHERE id = @Category_Id) IS NULL
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('This is a root category and it can not be renamed!', 17, 1)
		RETURN 1
	END
	
	-- Check if parent category contains another category with this name
	IF EXISTS (SELECT cat.id 
		FROM categories cat, categories cat1
		WHERE cat.parent_id = cat1.parent_id AND 
			cat.category_name = @New_Name AND 
			cat1.id = @Category_Id AND cat.id <> cat1.id) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Another category with this name already exists!', 17, 1)
		RETURN 1
	END
	
	UPDATE categories SET category_name = @New_Name 
	WHERE id = @Category_Id
	
	IF (@@ERROR <> 0 OR @@ROWCOUNT <> 1) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Changing category name was not successfull!', 17, 1)
		RETURN 1
	END
	
	COMMIT TRANSACTION
	RETURN 0


GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE PROCEDURE dbo.sp_ChangeImageInfo
(
	@Image_Id INT,
	@New_Name VARCHAR(50), 
	@New_Description VARCHAR(500),
	@User_Id INT
)
AS
	-- SET TRANSACTION ISOLATION LEVEL SERIALIZABLE	
	BEGIN TRANSACTION
	
	-- Check if user has access to this image
	DECLARE @Has_Access BIT
	
	EXECUTE sp_HasUserAccessToImage @User_Id, 
		@Image_Id, @Has_Access OUTPUT
	
	IF (@Has_Access <> 1) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Access denied!', 17, 1)
		RETURN 1
	END
	
	-- Check if the parent category contains another picture with the same name
	IF EXISTS (SELECT img.id FROM images img, images img1
		WHERE img.category_id = img1.category_id AND  
			img.file_name = @New_Name AND 
			img1.id = @Image_Id AND img.id <> img1.id)
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Image with this filename already exists in the category!', 17, 1)
		RETURN 1
	END
	
	-- Edit the image
	UPDATE images SET images.file_name = @New_Name, 
		description = @New_Description 
	WHERE id = @Image_Id
	
	IF (@@ERROR <> 0 OR @@ROWCOUNT <> 1) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Changing image info was not successfull!', 17, 1)
		RETURN 1
	END
	
	COMMIT TRANSACTION
	RETURN 0
 


GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

-- Changes the password and info for legimate users
CREATE PROCEDURE dbo.sp_ChangeUserInfo
(
	@Alias VARCHAR(50),
	@Password VARCHAR(50), 
	@New_Password VARCHAR(50), 
	@Name VARCHAR(50), 
	@Surname VARCHAR(50)
)
AS
	BEGIN TRANSACTION
	
	DECLARE @User_Id INT
	
	-- change the password
	EXEC sp_ChangeUserPassword 
		@Alias, @Password, @New_Password, @User_Id OUTPUT
	
	IF (@@ERROR <> 0 OR @User_Id IS NULL) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Changing password was not successfull!', 17, 1)
		RETURN 1
	END
	
	UPDATE user_profiles
	SET user_name = @Name, user_surname = @Surname
	WHERE id = @User_Id
	
	IF (@@ERROR <> 0 OR @@ROWCOUNT <> 1) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Updating user info was not successfull!', 17, 1)
		RETURN 1
	END
	
	COMMIT TRANSACTION
	RETURN 0 


GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

-- Changes the password for legimate users
CREATE PROCEDURE dbo.sp_ChangeUserPassword
(
	@Alias VARCHAR(50),
	@Password VARCHAR(50), 
	@New_Password VARCHAR(50), 
	@User_Id INT OUTPUT
)
AS
	-- SET TRANSACTION ISOLATION LEVEL SERIALIZABLE
	BEGIN TRANSACTION
	
	SELECT @User_Id = NULL
	
	-- Find the user
	SELECT @User_Id = (
		SELECT id FROM user_profiles 
		WHERE alias = @Alias AND 
		password = @Password)
		
	IF (@User_Id IS NULL)
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('No such user or password incorrect!', 17, 1)
		RETURN 1
	END
	
	-- Check password length
	IF (SELECT DATALENGTH(@New_Password)) < 3
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR ('Password must have at least 3 characters!', 17, 1)
		RETURN 1
	END
	
	UPDATE user_profiles SET password = @New_Password 
	WHERE id = @User_Id
	
	IF (@@ERROR <> 0 OR @@ROWCOUNT <> 1) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Changing password was not successfull!', 17, 1)
		RETURN 1
	END
	
	COMMIT TRANSACTION
	RETURN 0 


GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE PROCEDURE dbo.sp_CreateNewCategory
(
	@Name VARCHAR(100),
	@Parent_Id INT, 
	@User_Id INT,
	@Category_Id INT OUTPUT, 
	@Date_Modified DATETIME OUTPUT 
)

AS
	-- SET TRANSACTION ISOLATION LEVEL SERIALIZABLE	
	BEGIN TRANSACTION
	
	SELECT @Category_Id = -1
	
	-- Get user of parent category
	DECLARE @Parent_User_Id int 
	
	SELECT @Parent_User_Id = ( 
		SELECT user_id FROM categories 
		WHERE categories.id = @Parent_Id)
	
	IF (@Parent_User_Id IS NULL) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Parent not found!', 17, 1)
		RETURN 1
	END
	
	IF (@User_Id <> @Parent_User_Id)
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Access denied!', 17, 1)
		RETURN 1
	END
	
	-- Check if parent category contains another category with this name
	IF EXISTS (SELECT id FROM categories 
		WHERE parent_id = @Parent_Id AND category_name = @Name) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Another category with this name already exists!', 17, 1)
		RETURN 1
	END
	
	SELECT @Date_Modified = CURRENT_TIMESTAMP
	
	INSERT INTO categories 
		(category_name, date_modified, parent_id, user_id)
	VALUES (@Name, @Date_Modified, @Parent_Id, @User_Id)
	
	IF (@@ERROR <> 0 OR @@ROWCOUNT <> 1) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Inserting new category was not successfull!', 17, 1)
		RETURN 1
	END
	
	SELECT @Category_Id = @@IDENTITY
	
	-- If the parent category is shared automaticaly 
	-- share the newly inserted category
	IF (SELECT shared FROM categories 
		WHERE id = @Parent_Id) = 1
	BEGIN
		UPDATE categories SET shared = 1 WHERE id = @Category_Id 
	END 
		
	COMMIT TRANSACTION
	RETURN  0

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

-- For internal use only
CREATE PROCEDURE dbo.sp_CreateNewFormat
(
	@Format_Extention VARCHAR(3),
	@Format_Id INT OUTPUT
)
AS
	BEGIN TRANSACTION
	
	SELECT @Format_Id = -1
	
	INSERT INTO formats (format_name)
	VALUES (@Format_Extention)
	
	IF (@@ERROR <> 0 OR @@ROWCOUNT <> 1) BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Inserting new format was not successfull!', 17, 1)
		RETURN 1
	END
	
	SELECT @Format_Id = @@IDENTITY
	
	COMMIT TRANSACTION
	RETURN 0 

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE PROCEDURE dbo.sp_CreateNewImage
(
	@File_Name VARCHAR(100),
	@Image IMAGE,
	@Size BIGINT,  
	@Description VARCHAR(500),
	@Format_Name VARCHAR(5),  
	@Category_Id int, 
	@User_Id INT, 
	@Image_Id int OUTPUT, 
	@Date_Modified DATETIME OUTPUT
)
AS	
	-- SET TRANSACTION ISOLATION LEVEL SERIALIZABLE
	BEGIN TRANSACTION
	
	SELECT @Image_Id = -1
	
	-- Check if user has access to parent category
	DECLARE @Has_Access BIT
	
	EXECUTE sp_HasUserAccessToCategory @User_Id, 
		@Category_Id, @Has_Access OUTPUT
	
	IF (@Has_Access <> 1) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Creating new image was not successful! Access denied!', 17, 1)
		RETURN 1
	END
	
	-- Find the format with the specified name
	DECLARE @Format_Id int
	
	SELECT @Format_Id = (
		SELECT id FROM formats 
		WHERE formats.format_name = @Format_Name)
	
	IF (@Format_Id IS NULL) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Format not suported!', 17, 1)
		RETURN 1
	END
	
	-- Check if the parent category contains another picture with the same name
	IF EXISTS (SELECT id FROM images 
		WHERE category_id = @Category_Id AND 
		images.file_name = @File_Name)
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Image with this filename already exists in the category!', 17, 1)
		RETURN 1
	END
	
	SELECT @Date_Modified = CURRENT_TIMESTAMP
	
	-- Insert the image
	INSERT INTO images (file_name, image, size, date_modified, 
		description, format_id, category_id)
	VALUES (@File_Name, @Image, @Size, @Date_Modified, 
		@Description, @Format_Id, @Category_Id)
	
	IF (@@ERROR <> 0 OR @@ROWCOUNT <> 1) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Inserting new image was not successfull!', 17, 1)
		RETURN 1
	END
	
	SELECT @Image_Id = @@IDENTITY
	
	-- If the category is shared automaticaly share the newly inserted image
	IF (SELECT shared FROM categories 
		WHERE id = @Category_Id) = 1
	BEGIN
		UPDATE images SET shared = 1 WHERE id = @Image_Id 
	END 
	
	COMMIT TRANSACTION
	RETURN 0

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

-- Creates a new user and a root category of this user
CREATE PROCEDURE dbo.sp_CreateNewUser
(
	@Alias VARCHAR(50),
	@Password VARCHAR(50), 
	@Name VARCHAR(50), 
	@Surname VARCHAR(50), 
	@User_Id INT OUTPUT, 
	@Root_Category_Id INT OUTPUT
)
AS
	BEGIN TRANSACTION
	
	SELECT @User_Id = -1
	SELECT @Root_Category_Id = -1
	
	-- Check for existing user with this alias
	IF EXISTS (SELECT * FROM user_profiles WHERE alias = @Alias)
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR ('There is another user with this alias!', 17, 1)
		RETURN 1
	END
	
	IF (SELECT DATALENGTH(@Password)) < 3
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR ('Password must have at least 3 characters!', 17, 1)
		RETURN 1
	END
	
	INSERT INTO user_profiles 
		(alias, password, user_name, user_surname, active)
	VALUES (@Alias, @Password, @Name, @Surname, 1)
	
	IF (@@ERROR <> 0 OR @@ROWCOUNT <> 1) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Inserting new user was not successfull!', 17, 1)
		RETURN 1
	END
	
	SELECT @User_Id = @@IDENTITY
	
	-- automaticaly create a root category named 
	-- as the alias for every new user
	INSERT INTO categories 
		(category_name, date_modified, user_id)
	VALUES (@Alias, CURRENT_TIMESTAMP, @User_Id)
	
	IF (@@ERROR <> 0 OR @@ROWCOUNT <> 1) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Inserting root category for 
			user was not successfull!', 17, 1)
		RETURN @User_Id
	END
	
	SELECT @Root_Category_Id = @@IDENTITY
	
	COMMIT TRANSACTION
	RETURN 0


GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE PROCEDURE dbo.sp_DeleteCategory
(
	@Category_Id INT,
	@User_Id INT
)
AS
	-- SET TRANSACTION ISOLATION LEVEL SERIALIZABLE
	BEGIN TRANSACTION
	
	-- Check if user has access to this category
	DECLARE @Has_Access BIT
	
	EXECUTE sp_HasUserAccessToCategory @User_Id, 
		@Category_Id, @Has_Access OUTPUT
	
	IF (@Has_Access <> 1) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Deleting category was not successful! Access denied!', 17, 1)
		RETURN 1
	END
	
	-- Check if this is a root category	
	IF (SELECT parent_id FROM categories 
		WHERE id = @Category_Id) IS NULL
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('This is a root category and it can not be deleted!', 17, 1)
		RETURN 1
	END
	
----------------------- Begin logic ----------------------------------

	DECLARE @T TABLE(
		id INT, 
		category_name VARCHAR(100), 
		date_modified DATETIME, 
		parent_id INT, 
		user_id INT, 
		shared BIT, 
		status INT
	) 
	
	-- Insert into @T the root category 
	INSERT INTO @T(id, category_name, date_modified, 
		parent_id, user_id, shared)
	SELECT * FROM categories WHERE id = @Category_Id
	
	-- Set the root category status to 0 (parent)
	UPDATE @T SET status = 0 WHERE status IS NULL
	
	WHILE @@ROWCOUNT > 0
	BEGIN
		-- Insert into @T all the childs of existing parents in @T
		INSERT INTO @T(id, category_name, date_modified, 
			parent_id, user_id, shared) 
		SELECT * FROM categories WHERE parent_id IN (
			SELECT id FROM @T WHERE status = 0)
		
		-- Set the newly inserted categories' status to 1 (childs)		
		UPDATE @T SET status = 1 WHERE status IS NULL
		
		-- Delete all parent categories
		DELETE FROM categories WHERE id IN (
			SELECT id FROM @T WHERE status = 0)
								
--------------------------- Interupt logic --------------------------------		
		IF (@@ERROR <> 0) 
		BEGIN
			ROLLBACK TRANSACTION
			RAISERROR('Operation failed!', 17, 1)
			RETURN 1
		END
----------------------------- Continue logic --------------------------
		
		-- Set all deleted categories' status to -1 (deleted)
		UPDATE @T SET status = -1 WHERE status = 0
		
		-- Make all childs current parents
		UPDATE @T SET status = 0 WHERE status = 1
	END
	
------------------------------ End logic -------------------------

	IF (@@ERROR <> 0) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Delete operation failed!', 17, 1)
		RETURN 1
	END
	
	COMMIT TRANSACTION
	RETURN 0 
GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

-- for internal use only
CREATE PROCEDURE dbo.sp_DeleteFormat
(
	@Format_Id INT,
	@Successfull BIT = 0 OUTPUT
)
AS
	BEGIN TRANSACTION
	
	SELECT @Successfull = 0
	
	DELETE FROM formats WHERE id = @Format_Id
	
	IF (@@ERROR <> 0 OR @@ROWCOUNT <> 1) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Deleting format 
			was not successfull!', 17, 1)
		RETURN 1
	END
	
	SELECT @Successfull = 1
	
	COMMIT TRANSACTION
	RETURN 0

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

-- Deletes the image only if user has access
CREATE PROCEDURE dbo.sp_DeleteImage
(
	@Image_Id INT,
	@User_Id INT
)
AS
	BEGIN TRANSACTION
	
	-- Check if user has access to this image
	DECLARE @Has_Access BIT
	
	EXECUTE sp_HasUserAccessToImage @User_Id, @Image_Id, @Has_Access OUTPUT
	
	IF (@Has_Access <> 1) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Deleting image was not successfull! Access denied!', 17, 1)
		RETURN 1
	END
	
	-- Delete the image
	DELETE FROM images WHERE id = @Image_Id
	
	IF (@@ERROR <> 0 OR @@ROWCOUNT <> 1) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Deleting image 
			was not successfull!', 17, 1)
		RETURN 1
	END
	
	COMMIT TRANSACTION
	RETURN 0
 

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

-- for internal use only
-- admin should use this procedure to delete users
CREATE PROCEDURE dbo.sp_DeleteUserById
(
	@User_Id INT
)
AS
	BEGIN TRANSACTION
	
	-- Check if this user exists
	IF NOT EXISTS (
		SELECT * FROM user_profiles 
		WHERE id = @User_Id)
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('No user with this id exists!', 17, 1)
		RETURN 1
	END
	
	-- set user not active
	UPDATE user_profiles SET active = 0 WHERE id = @User_Id
	
	IF (@@ERROR <> 0 OR @@ROWCOUNT <> 1) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Could not update user!', 17, 1)
		RETURN 1
	END
	
	-- Deleting a user deletes all his categories
	-- becouse of the relationship. Deleting a category
	-- forces the deletion of all its images 
	-- (again, because of the relationship).
	DELETE FROM user_profiles WHERE id = @User_Id
	
	IF (@@ERROR <> 0 OR @@ROWCOUNT <> 1) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Deleting user was not successfull!', 17, 1)
		RETURN 1
	END
	
	COMMIT TRANSACTION
	RETURN 0

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO


-- A user can delete all his pictures only after
-- giving his password again
CREATE PROCEDURE dbo.sp_DeleteUserByPass
(
	@User_Alias VARCHAR(50),
	@User_Password VARCHAR(50)
)
AS
	BEGIN TRANSACTION
	
	DECLARE @User_Id int
	
	SELECT @User_Id = (
		SELECT id FROM user_profiles 
		WHERE alias = @User_Alias AND 
		password = @User_Password)
	
	IF @User_Id IS NULL
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('No user with this alias or password incorrect!', 17, 1)
		RETURN 1
	END
	
	-- Deleting a user deletes all his categories
	-- becouse of the relationship. Deleting a category
	-- forces the deletion of all its images 
	-- (again, because of the relationship).
	DELETE FROM user_profiles WHERE id = @User_Id
	
	IF (@@ERROR <> 0 OR @@ROWCOUNT <> 1) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Deleting user was not successfull!', 17, 1)
		RETURN 1
	END
	
	COMMIT TRANSACTION
	RETURN 0


GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE PROCEDURE dbo.sp_GetAllSubcategoryIds
(
	@Category_Id INT
)
AS
	SELECT id FROM categories 
	WHERE parent_id = @Category_Id
	
	RETURN 

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE PROCEDURE dbo.sp_GetCategory	
(
	@Category_Id INT
)
AS
	SELECT cat.category_name, 
		cat.date_modified, 
		cat.shared, 
		usr.user_name + ' ' + usr.user_surname AS owner
	FROM categories cat 
	INNER JOIN user_profiles usr ON cat.user_id = usr.id 
	WHERE cat.id = @Category_Id

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE PROCEDURE dbo.sp_GetImage	
(
	@Image_Id INT
)
AS
	SELECT image FROM images WHERE id = @Image_Id
GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE PROCEDURE dbo.sp_GetImageIdsInCategory
(
	@Category_Id INT
)
AS
	SELECT id AS image_id FROM images WHERE category_id = @Category_Id

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE PROCEDURE dbo.sp_GetImageInfo	
(
	@Image_Id INT
)
AS
	SELECT 
		img.file_name AS name, 
		img.size AS size, 
		img.date_modified AS date_modified,	
		img.description AS description, 
		img.shared AS shared,
		formats.format_name AS format,
		usr.user_name + ' ' + usr.user_surname AS owner    
	FROM images img 
	INNER JOIN formats ON img.format_id = formats.id 
	INNER JOIN categories cat ON img.category_id = cat.id 
	INNER JOIN user_profiles usr ON cat.user_id = usr.id
	WHERE img.id = @Image_Id
	

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE PROCEDURE dbo.sp_GetImagesInCategory
(
	@Category_Id INT
)
AS
	SELECT id, images.file_name, images.size, 
		images.date_modified, images.description 
	FROM images WHERE category_id = @Category_Id

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE PROCEDURE dbo.sp_GetRootCategoryId	
(
	@User_Id INT
)
AS
	SELECT cat.id FROM categories cat 
	INNER JOIN user_profiles users ON cat.user_id = users.id 
	WHERE users.id = @User_Id AND cat.parent_id IS NULL

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

-- All users are alowed to view the size of either 
-- their own or shared categories
CREATE PROCEDURE dbo.sp_GetSizeOfCategory
(
	@Category_Id INT,
	@Size BIGINT = 0 OUTPUT
)
AS
	SET TRANSACTION ISOLATION LEVEL SERIALIZABLE
	BEGIN TRANSACTION
	
	SELECT @Size = 0
	
----------------------- Begin logic ----------------------------------

	DECLARE @T TABLE(
		id INT, 
		category_name VARCHAR(170), 
		date_modified DATETIME, 
		parent_id INT, 
		user_id INT, 
		shared BIT, 
		status INT
	) 
	
	-- Insert into @T the root category 
	INSERT INTO @T(id, category_name, date_modified, 
		parent_id, user_id, shared)
	SELECT * FROM categories WHERE id = @Category_Id
	
	-- Set the root category status to 0 (parent)
	UPDATE @T SET status = 0 WHERE status IS NULL
	
	DECLARE @Current_Size BIGINT
	WHILE @@ROWCOUNT > 0
	BEGIN
		-- Insert into @T all the childs of existing parents in @T
		INSERT INTO @T(id, category_name, date_modified, 
			parent_id, user_id, shared) 
		SELECT * FROM categories WHERE parent_id IN (
			SELECT id FROM @T WHERE status = 0)
		
		-- Set the newly inserted categories' status to 1 (childs)		
		UPDATE @T SET status = 1 WHERE status IS NULL
		
		SELECT @Current_Size = (
			SELECT sum(img.size) FROM images AS img
			INNER JOIN categories AS cat ON img.category_id = cat.id 
			WHERE img.category_id IN (SELECT id FROM @T WHERE status = 0)
			GROUP BY cat.user_id) 
			
		IF (@Current_Size IS NOT NULL) 
		BEGIN
			SELECT @Size = @Size + @Current_Size
		END 
		
		-- Set all proccesed categories' status to -1 (deleted)
		UPDATE @T SET status = -1 WHERE status = 0
		
		-- Make all childs current parents
		UPDATE @T SET status = 0 WHERE status = 1
	END
	
------------------------------ End logic -------------------------

	IF (@@ERROR <> 0) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Operatioon failed!', 17, 1)
		RETURN 1
	END
	
	COMMIT TRANSACTION
	RETURN 0

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE PROCEDURE dbo.sp_GetSubcategories
(
	@Category_Id INT
)

AS
	SELECT * FROM categories 
	WHERE parent_id = @Category_Id
	
	 

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE PROCEDURE dbo.sp_GetSubcategoryIds
(
	@Category_Id INT
)
AS
	SELECT id FROM categories 
	WHERE parent_id = @Category_Id
	

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE PROCEDURE dbo.sp_GetUser	
(
	@User_Id INT
)
AS
	SELECT users.alias, users.user_name, users.user_surname, 
		cat.id AS root_category_id 
	FROM categories cat 
	INNER JOIN user_profiles users ON cat.user_id = users.id 
	WHERE users.id = @User_Id AND cat.parent_id IS NULL
	
	RETURN @@ROWCOUNT

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE PROCEDURE dbo.sp_HasUserAccessToCategory
(
	@User_Id INT, 
	@Category_Id INT, 
	@Has_Access BIT OUTPUT
)
AS
	IF NOT EXISTS (
		SELECT id FROM categories 
		WHERE id = @Category_Id AND user_id = @User_Id)
		SELECT @Has_Access = 0		
	ELSE
		SELECT @Has_Access = 1
	RETURN 0
GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

-- For internal use only 
-- (Raises error and rollbacks if user don't have access)
CREATE PROCEDURE dbo.sp_HasUserAccessToImage
(
	@User_Id INT, 
	@Image_Id INT, 
	@Has_Access BIT OUTPUT
)
AS	
	IF NOT EXISTS (SELECT img.id FROM images img 
		INNER JOIN categories cat ON img.category_id = cat.id 
		WHERE img.id = @Image_Id AND cat.user_id = @User_Id)
		SELECT @Has_Access = 0		
	ELSE
		SELECT @Has_Access = 1


GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO


CREATE PROCEDURE dbo.sp_PerformLogin
(
	@Loginame varchar(50),
	@Password varchar(50), 
	@User_Id INT OUTPUT
)
AS
	BEGIN TRANSACTION
	
	SELECT @User_Id = -1
	
	-- Check for users with this alias
	IF NOT EXISTS (
		SELECT * FROM user_profiles 
		WHERE alias = @Loginame)
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('No user with this alias!', 17, 1)
		RETURN 1
	END
	
	-- Find the user
	SELECT @User_Id = (
		SELECT id FROM user_profiles 
		WHERE alias = @Loginame AND password = @Password)
		
	IF (@User_Id = -1 OR @User_Id IS NULL) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Password incorrect!', 17, 1)
		RETURN 1
	END
	
	-- Do not allow one user to log in multiple times 
	IF (SELECT active FROM user_profiles WHERE id = @User_Id) = 1
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('User already logged in!', 17, 1)
		RETURN 1
	END
	
	-- set user active
	UPDATE user_profiles SET active = 1 WHERE id = @User_Id
	
	IF (@@ERROR <> 0 OR @@ROWCOUNT <> 1) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Could not update user!', 17, 1)
		RETURN 1
	END
	
	COMMIT TRANSACTION
	RETURN 0


GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE PROCEDURE dbo.sp_PerformLogout
(
	@User_Id INT
)
AS
	BEGIN TRANSACTION
	
	-- Check if user is active
	IF (SELECT active FROM user_profiles 
		WHERE id = @User_Id) = 0 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('User is not active!', 17, 1)
		RETURN 1
	END
	
	-- set user not active
	UPDATE user_profiles SET active = 0 WHERE id = @User_Id
	
	IF (@@ERROR <> 0 OR @@ROWCOUNT <> 1) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Could not update user!', 17, 1)
		RETURN 1
	END
	
	COMMIT TRANSACTION
	RETURN 0

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

-- Shares the category and everything inside
-- Obhojdane v shirina!
CREATE PROCEDURE dbo.sp_ShareCategory
(
	@Category_Id INT,
	@User_Id INT
)
AS
	BEGIN TRANSACTION
	
	-- Check if user has access to this category
	DECLARE @Has_Access BIT
	
	EXECUTE sp_HasUserAccessToCategory @User_Id, 
		@Category_Id, @Has_Access OUTPUT
	
	IF (@Has_Access <> 1) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Sharing category was not successfull! Access denied!', 17, 1)
		RETURN 1
	END
	
	-- check if category is already shared
	IF (SELECT shared FROM categories 
		WHERE id = @Category_Id) = 1
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Category already shared!', 17, 1)
		RETURN 1
	END
	
	-- Check if this is a root category	
	IF (SELECT parent_id FROM categories 
		WHERE id = @Category_Id) IS NULL
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('This is a root category and it can not be shared!', 17, 1)
		RETURN 1
	END
	
---------------------------- Begin logic ----------------------------------

	DECLARE @T TABLE(
		id INT, 
		category_name VARCHAR(170), 
		date_modified DATETIME, 
		parent_id INT, 
		user_id INT, 
		shared BIT, 
		status INT
	) 
	
	-- Insert into @T the root category 
	INSERT INTO @T(id, category_name, date_modified, 
		parent_id, user_id, shared)
	SELECT * FROM categories WHERE id = @Category_Id
	
	-- Set the root category status to 0 (parent)
	UPDATE @T SET status = 0 WHERE status IS NULL
	
	WHILE @@ROWCOUNT > 0
	BEGIN
		-- Insert into @T all the childs of existing parents in @T
		INSERT INTO @T(id, category_name, date_modified, 
			parent_id, user_id, shared) 
		SELECT * FROM categories WHERE parent_id IN (
			SELECT id FROM @T WHERE status = 0)
		
		-- Set the newly inserted categories' status to 1 (childs)		
		UPDATE @T SET status = 1 WHERE status IS NULL
		
		-- Share all parent categories and their images
		UPDATE categories SET shared = 1 WHERE id IN (
			SELECT id FROM @T WHERE status = 0)
		
		UPDATE images SET shared = 1 WHERE category_id IN (
			SELECT id FROM @T WHERE status = 0)

								
--------------------------- Interupt logic --------------------------------		
		IF (@@ERROR <> 0) 
		BEGIN
			ROLLBACK TRANSACTION
			RAISERROR('Operation failed!', 17, 1)
			RETURN 1
		END
----------------------------- Continue logic --------------------------
		
		-- Set all deleted categories' status to -1 (deleted)
		UPDATE @T SET status = -1 WHERE status = 0
		
		-- Make all childs current parents
		UPDATE @T SET status = 0 WHERE status = 1
	END
	
------------------------------ End logic -------------------------

	IF (@@ERROR <> 0) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Operatioon failed!', 17, 1)
		RETURN 1
	END
	
	COMMIT TRANSACTION
	RETURN 0 

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE PROCEDURE dbo.sp_ShareImage 
(
	@Image_Id INT,
	@User_Id INT
)
AS	
	BEGIN TRANSACTION
	
	-- Check if user has access to this image
	DECLARE @Has_Access BIT
	
	EXECUTE sp_HasUserAccessToImage @User_Id, 
		@Image_Id, @Has_Access OUTPUT
	
	IF (@Has_Access <> 1) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Sharing image was not successfull! Access denied!', 17, 1)
		RETURN 1
	END
	
	-- Check if image is already shared
	IF (SELECT shared FROM images  
		WHERE id = @Image_Id) = 1
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Sharing image was not successfull! Image already shared!', 17, 1)
		RETURN 1
	END
	
	-- Share the image
	UPDATE images SET shared = 1 WHERE id = @Image_Id
	
	IF (@@ERROR <> 0 OR @@ROWCOUNT <> 1) 
	BEGIN
		ROLLBACK TRANSACTION
		RAISERROR('Sharing image was not successfull!', 17, 1)
		RETURN 1
	END
	
	COMMIT TRANSACTION
	RETURN 0
 

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO


exec sp_addextendedproperty N'MS_Description', null, N'user', N'dbo', N'table', N'formats', N'column', N'id'


GO

