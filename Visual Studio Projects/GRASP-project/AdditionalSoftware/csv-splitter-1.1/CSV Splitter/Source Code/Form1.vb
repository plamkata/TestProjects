Imports System.IO
Imports System.Text

Public Class Form1
    Inherits System.Windows.Forms.Form

#Region " Windows Form Designer generated code "

    <STAThread()> _
    Public Shared Sub Main()
        Application.EnableVisualStyles()
        Application.Run(New Form1)
    End Sub


    Public Sub New()
        MyBase.New()

        'This call is required by the Windows Form Designer.
        InitializeComponent()

        'Add any initialization after the InitializeComponent() call

    End Sub

    'Form overrides dispose to clean up the component list.
    Protected Overloads Overrides Sub Dispose(ByVal disposing As Boolean)
        If disposing Then
            If Not (components Is Nothing) Then
                components.Dispose()
            End If
        End If
        MyBase.Dispose(disposing)
    End Sub

    'Required by the Windows Form Designer
    Private components As System.ComponentModel.IContainer

    'NOTE: The following procedure is required by the Windows Form Designer
    'It can be modified using the Windows Form Designer.  
    'Do not modify it using the code editor.
    Friend WithEvents OpenFileDialog1 As System.Windows.Forms.OpenFileDialog
    Friend WithEvents btnBrowse As System.Windows.Forms.Button
    Friend WithEvents txtCSV As System.Windows.Forms.TextBox
    Friend WithEvents Label1 As System.Windows.Forms.Label
    Friend WithEvents Label2 As System.Windows.Forms.Label
    Friend WithEvents Label3 As System.Windows.Forms.Label
    Friend WithEvents lblStatus As System.Windows.Forms.Label
    Friend WithEvents btnSplit As System.Windows.Forms.Button
    Friend WithEvents numLinesCount As System.Windows.Forms.NumericUpDown
    Friend WithEvents numMaxFiles As System.Windows.Forms.NumericUpDown
    Friend WithEvents btnAbort As System.Windows.Forms.Button
    <System.Diagnostics.DebuggerStepThrough()> Private Sub InitializeComponent()
        Me.btnSplit = New System.Windows.Forms.Button
        Me.txtCSV = New System.Windows.Forms.TextBox
        Me.OpenFileDialog1 = New System.Windows.Forms.OpenFileDialog
        Me.btnBrowse = New System.Windows.Forms.Button
        Me.Label1 = New System.Windows.Forms.Label
        Me.Label2 = New System.Windows.Forms.Label
        Me.Label3 = New System.Windows.Forms.Label
        Me.lblStatus = New System.Windows.Forms.Label
        Me.numLinesCount = New System.Windows.Forms.NumericUpDown
        Me.numMaxFiles = New System.Windows.Forms.NumericUpDown
        Me.btnAbort = New System.Windows.Forms.Button
        CType(Me.numLinesCount, System.ComponentModel.ISupportInitialize).BeginInit()
        CType(Me.numMaxFiles, System.ComponentModel.ISupportInitialize).BeginInit()
        Me.SuspendLayout()
        '
        'btnSplit
        '
        Me.btnSplit.FlatStyle = System.Windows.Forms.FlatStyle.System
        Me.btnSplit.Location = New System.Drawing.Point(384, 48)
        Me.btnSplit.Name = "btnSplit"
        Me.btnSplit.Size = New System.Drawing.Size(88, 24)
        Me.btnSplit.TabIndex = 0
        Me.btnSplit.Text = "Split Now!"
        '
        'txtCSV
        '
        Me.txtCSV.Location = New System.Drawing.Point(104, 16)
        Me.txtCSV.Name = "txtCSV"
        Me.txtCSV.Size = New System.Drawing.Size(264, 20)
        Me.txtCSV.TabIndex = 1
        Me.txtCSV.Text = "C:\"
        '
        'OpenFileDialog1
        '
        Me.OpenFileDialog1.Title = "Select the CSV file to be splitted"
        '
        'btnBrowse
        '
        Me.btnBrowse.FlatStyle = System.Windows.Forms.FlatStyle.System
        Me.btnBrowse.Location = New System.Drawing.Point(384, 16)
        Me.btnBrowse.Name = "btnBrowse"
        Me.btnBrowse.Size = New System.Drawing.Size(88, 24)
        Me.btnBrowse.TabIndex = 4
        Me.btnBrowse.Text = "Browse File"
        '
        'Label1
        '
        Me.Label1.FlatStyle = System.Windows.Forms.FlatStyle.System
        Me.Label1.Location = New System.Drawing.Point(8, 16)
        Me.Label1.Name = "Label1"
        Me.Label1.Size = New System.Drawing.Size(56, 16)
        Me.Label1.TabIndex = 5
        Me.Label1.Text = "CSV File:"
        '
        'Label2
        '
        Me.Label2.FlatStyle = System.Windows.Forms.FlatStyle.System
        Me.Label2.Location = New System.Drawing.Point(8, 48)
        Me.Label2.Name = "Label2"
        Me.Label2.Size = New System.Drawing.Size(96, 16)
        Me.Label2.TabIndex = 6
        Me.Label2.Text = "Number of Lines:"
        '
        'Label3
        '
        Me.Label3.FlatStyle = System.Windows.Forms.FlatStyle.System
        Me.Label3.Location = New System.Drawing.Point(8, 80)
        Me.Label3.Name = "Label3"
        Me.Label3.Size = New System.Drawing.Size(72, 16)
        Me.Label3.TabIndex = 7
        Me.Label3.Text = "Max Pieces:"
        '
        'lblStatus
        '
        Me.lblStatus.Font = New System.Drawing.Font("Microsoft Sans Serif", 9.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.lblStatus.Location = New System.Drawing.Point(8, 120)
        Me.lblStatus.Name = "lblStatus"
        Me.lblStatus.Size = New System.Drawing.Size(456, 16)
        Me.lblStatus.TabIndex = 8
        '
        'numLinesCount
        '
        Me.numLinesCount.Increment = New Decimal(New Integer() {1000, 0, 0, 0})
        Me.numLinesCount.Location = New System.Drawing.Point(104, 48)
        Me.numLinesCount.Maximum = New Decimal(New Integer() {10000000, 0, 0, 0})
        Me.numLinesCount.Name = "numLinesCount"
        Me.numLinesCount.Size = New System.Drawing.Size(96, 20)
        Me.numLinesCount.TabIndex = 9
        Me.numLinesCount.Value = New Decimal(New Integer() {50000, 0, 0, 0})
        '
        'numMaxFiles
        '
        Me.numMaxFiles.Location = New System.Drawing.Point(104, 80)
        Me.numMaxFiles.Maximum = New Decimal(New Integer() {100000, 0, 0, 0})
        Me.numMaxFiles.Name = "numMaxFiles"
        Me.numMaxFiles.Size = New System.Drawing.Size(96, 20)
        Me.numMaxFiles.TabIndex = 10
        '
        'btnAbort
        '
        Me.btnAbort.Enabled = False
        Me.btnAbort.FlatStyle = System.Windows.Forms.FlatStyle.System
        Me.btnAbort.Location = New System.Drawing.Point(384, 80)
        Me.btnAbort.Name = "btnAbort"
        Me.btnAbort.Size = New System.Drawing.Size(88, 24)
        Me.btnAbort.TabIndex = 11
        Me.btnAbort.Text = "Abort"
        '
        'Form1
        '
        Me.AutoScaleBaseSize = New System.Drawing.Size(5, 13)
        Me.ClientSize = New System.Drawing.Size(490, 160)
        Me.Controls.Add(Me.btnAbort)
        Me.Controls.Add(Me.numMaxFiles)
        Me.Controls.Add(Me.numLinesCount)
        Me.Controls.Add(Me.lblStatus)
        Me.Controls.Add(Me.btnBrowse)
        Me.Controls.Add(Me.txtCSV)
        Me.Controls.Add(Me.btnSplit)
        Me.Controls.Add(Me.Label3)
        Me.Controls.Add(Me.Label2)
        Me.Controls.Add(Me.Label1)
        Me.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle
        Me.MaximizeBox = False
        Me.MaximumSize = New System.Drawing.Size(496, 192)
        Me.MinimumSize = New System.Drawing.Size(496, 192)
        Me.Name = "Form1"
        Me.Text = "FXFisherman's CSV Splitter v1.1 by Sopheap Ly"
        CType(Me.numLinesCount, System.ComponentModel.ISupportInitialize).EndInit()
        CType(Me.numMaxFiles, System.ComponentModel.ISupportInitialize).EndInit()
        Me.ResumeLayout(False)

    End Sub

#End Region


    Public Delegate Sub UpdateProgressSub(ByVal CurrentLine As Integer)
    Private _IsAbort As Boolean


    Sub SplitCSV(ByVal FilePath As String, ByVal LineCount As Integer, ByVal MaxOutputFile As Integer, ByVal Status As UpdateProgressSub, ByRef IsAbort As Boolean)

        ' Validate first
        If LineCount < 100 Then Throw New Exception("Number of lines must be more than 100.")

        ' Open the csv file for reading
        Dim Reader As New IO.StreamReader(FilePath)

        ' Create the output directory
        Dim OutputFolder As String = FilePath & "_Pieces"
        If Directory.Exists(FilePath) = False Then
            Directory.CreateDirectory(OutputFolder)
        End If

        ' Read the csv column's header
        Dim strHeader As String = Reader.ReadLine

        ' Start splitting
        Dim FileIndex As Integer

        Do

            ' Update progress
            FileIndex += 1
            If Not Status Is Nothing Then
                Status.Invoke((FileIndex - 1) * LineCount)
            End If

            ' Check if the number of splitted files doesn't exceed the limit
            If (MaxOutputFile < FileIndex) And (MaxOutputFile > 0) Then Exit Do

            ' Create new file to store a piece of the csv file
            Dim PiecePath As String = OutputFolder & "\" & Path.GetFileNameWithoutExtension(FilePath) & "_" & FileIndex & Path.GetExtension(FilePath)
            Dim Writer As New StreamWriter(PiecePath, False)
            Writer.AutoFlush = False
            Writer.WriteLine(strHeader)

            ' Read and writes precise number of rows
            For i As Integer = 1 To LineCount

                Dim s As String = Reader.ReadLine()
                If s <> Nothing And _IsAbort = False Then
                    Writer.WriteLine(s)
                Else
                    Writer.Flush()
                    Writer.Close()
                    Exit Do
                End If

            Next

            ' Flush and close the splitted file
            Writer.Flush()
            Writer.Close()

        Loop

        Reader.Close()

    End Sub


    Private Sub btnSplit_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles btnSplit.Click
        Dim th As New Threading.Thread(AddressOf SplitIt)
        th.Start()
    End Sub


    Sub SplitIt()

        btnSplit.Enabled = False
        btnAbort.Enabled = True

        Try

            SplitCSV(txtCSV.Text, numLinesCount.Value, numMaxFiles.Value, AddressOf UpdateProgress, _IsAbort)

            If Not _IsAbort Then
                MsgBox("Completed Successfully!", MsgBoxStyle.Information)
            Else
                _IsAbort = False
                MsgBox("Spliting proccess was aborted by user.", MsgBoxStyle.Critical)
            End If

        Catch ex As Exception
            MsgBox("Unable to split the CSV File. Reason: " & ex.Message, MsgBoxStyle.Critical)
        Finally
            btnSplit.Enabled = True
            btnAbort.Enabled = False
        End Try

    End Sub


    Sub UpdateProgress(ByVal CurrentLine As Integer)
        lblStatus.Text = "Aprox. " & CurrentLine.ToString & " lines splitted"
    End Sub


    Private Sub btnBrowse_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles btnBrowse.Click
        If OpenFileDialog1.ShowDialog = DialogResult.OK Then
            txtCSV.Text = OpenFileDialog1.FileName()
        End If
    End Sub


    Private Sub btnAbort_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles btnAbort.Click
        _IsAbort = True
    End Sub


    Private Sub Form1_Closing(ByVal sender As Object, ByVal e As System.ComponentModel.CancelEventArgs) Handles MyBase.Closing
        _IsAbort = True
    End Sub

End Class
