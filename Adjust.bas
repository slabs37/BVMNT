B4A=true
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
	Dim updatingpreviews As Boolean
	Dim VoltSet As Boolean
	Dim AmpsSet As Boolean
	Dim su As SuShell
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.

	Private VoltDiv As EditText
	Private AmpsDiv As EditText
	Private VoltDivService As EditText
	Private VoltPreview As Label
	Private Back As Button
	Private WattPreview As Label
	Private VoltServiceSet As Button
	Private MinVol As EditText
	Private MinSet As Button
	Private MaxVol As EditText
	Private MaxSet As Button
	Private SuDumpT As EditText
	Private SuDumpTest As Button
	Private SuDumpSet As Button
	Private ExplSu As Label
	Private ExplSuMinMax As Label
	Private bgcolor3 As Label
	Private Explamp As Label
	Private Explvl As Label
	Private bgcolor1 As Label
	Private Expl As Label
End Sub

Sub Activity_Create(FirstTime As Boolean)
	updatingpreviews = False
	'Activity.LoadLayout("Layout1")
	Activity.LoadLayout("2")
	'check if voltage division exists
	Starter.CheckDivisions
	VoltDiv.Text = Starter.VolDiv
	AmpsDiv.Text = Starter.AmpDiv
	VoltDivService.Text = Starter.VolServiceDiv
	MaxVol.Text = Starter.HundredVol
	MinVol.Text = Starter.ZeroVol
	SuDumpT.Text = Starter.kvs.Get("BatteryServiceName")
	
	'check if volt and amp files are set
	If Starter.kvs.ContainsKey("VolLocation") Then
		If File.Exists(Starter.kvs.Get("BatteryPath"), Starter.kvs.Get("VolLocation")) Then
		VoltSet = True
		Else
		VoltSet = False
		End If
	Else
	VoltSet = False
	End If
	
	If Starter.kvs.ContainsKey("AmpLocation") Then
		If File.Exists(Starter.kvs.Get("BatteryPath"), Starter.kvs.Get("AmpLocation")) Then
			AmpsSet = True
		Else
			AmpsSet = False
		End If
	Else
		AmpsSet = False
	End If
	'update previews
	If updatingpreviews = False Then
		Preview_Update
	End If
	
	'showing su related things
	If su.DeviceRooted = True Then
		ExplSu.Visible = True
		SuDumpT.Visible = True
		SuDumpTest.Visible = True
		SuDumpSet.Visible = True
		ExplSuMinMax.Visible = True
		MinVol.Visible = True
		MinSet.Visible = True
		MaxSet.Visible = True
		MaxVol.Visible = True
		bgcolor3.Visible = True
	End If
	
	'showing watt related things
	If Starter.kvs.ContainsKey("VolLocation") And Starter.kvs.ContainsKey("AmpLocation") And Starter.kvs.ContainsKey("BatteryPath") Then
		Expl.Visible = True
		Explvl.Visible = True
		Explamp.Visible = True
		AmpsDiv.Visible = True
		VoltDiv.Visible = True
		WattPreview.Visible = True
		bgcolor1.Visible = True
	End If
End Sub

Sub Activity_Resume
	
End Sub

Sub Activity_Pause (UserClosed As Boolean)
End Sub

Sub Preview_Update
	updatingpreviews = True
	If VoltSet = True Then
		VoltPreview.Text = (File.ReadString(Starter.kvs.Get("BatteryPath"), Starter.kvs.Get("VolLocation"))/Starter.VolServiceDiv)
	End If
	If VoltSet = True And AmpsSet = True Then
		WattPreview.Text=(File.ReadString(Starter.kvs.Get("BatteryPath"), Starter.kvs.Get("VolLocation"))/Starter.VolDiv)*(File.ReadString(Starter.kvs.Get("BatteryPath"), Starter.kvs.Get("AmpLocation"))/Starter.AmpDiv)
	End If
	Sleep(500)
	Preview_Update
End Sub

Private Sub VoltDiv_EnterPressed
	Starter.VolDiv = VoltDiv.Text
	Starter.kvs.Put("VolDiv", Starter.VolDiv)
	ToastMessageShow("Set Voltage Division", False)
End Sub

Private Sub AmpsDiv_EnterPressed
	Starter.AmpDiv = AmpsDiv.Text
	Starter.kvs.Put("AmpDiv", Starter.AmpDiv)
	ToastMessageShow("Set Ampere Division", False)
End Sub

Private Sub VoltDivService_EnterPressed
	Starter.VolServiceDiv = VoltDivService.Text
	Starter.kvs.Put("VolServiceDiv", Starter.VolServiceDiv)
	ToastMessageShow("Set Voltage Division", False)
End Sub

Private Sub Back_Click
	Activity.Finish
End Sub

Private Sub VoltServiceSet_Click
	Starter.VolServiceDiv = VoltDivService.Text
	Starter.kvs.Put("VolServiceDiv", Starter.VolServiceDiv)
	ToastMessageShow("Set Voltage Division", False)
End Sub

Private Sub MinVol_EnterPressed
	Starter.ZeroVol = MinVol.Text
	Starter.kvs.Put("ZeroVol", MinVol.Text)
	ToastMessageShow("Set 0%", False)
End Sub
Private Sub MinSet_Click
	MinVol_EnterPressed
End Sub

Private Sub MaxVol_EnterPressed
	Starter.HundredVol = MaxVol.Text
	Starter.kvs.Put("HundredVol", MaxVol.Text)
	ToastMessageShow("Set 100%", False)
End Sub
Private Sub MaxSet_Click
	MaxVol_EnterPressed
End Sub

Private Sub SuDumpTest_Click
	ToastMessageShow("setting battery to 1%", False)
	Log(su.Execute("dumpsys " & SuDumpT.Text & " set level 1"))
	Sleep(1000)
	ToastMessageShow("resetting battery", False)
	Log(su.Execute("dumpsys " & SuDumpT.Text & " reset"))
End Sub

Private Sub SuDumpSet_Click
	Starter.kvs.Put("BatteryServiceName", SuDumpT.Text)
End Sub