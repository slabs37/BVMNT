﻿B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Activity
Version=11.8
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: False
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Dim p As Intent
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.

	Private ImageView1 As ImageView
	Private Back As Button
	Private Label4 As Label
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	'Activity.LoadLayout("Layout1")
	Activity.LoadLayout("3")
	ImageView1.Bitmap = LoadBitmap(File.DirAssets, "BVMNT.png")
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub


Private Sub Back_Click
	Activity.Finish
End Sub

Private Sub Label4_Click
	p.Initialize(p.ACTION_VIEW, "https://pooyahaghi.ir/en/")
	StartActivity(p)
End Sub