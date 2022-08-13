B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Service
Version=11.8
@EndOfDesignText@
#Region  Service Attributes 
	#StartAtBoot: False
	
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Dim StopNotif As Boolean
	Dim n As NB6
	Dim no As Notification
	Dim VoltFile As String
	Dim BatteryLoc As String
	Dim ShowNotif As Boolean
	Dim VoltDiv As Int
	Dim su As SuShell
	Dim loopongoing As Boolean
	
End Sub

Sub Service_Create
	Service.AutomaticForegroundMode = Service.AUTOMATIC_FOREGROUND_NEVER
	
	If Starter.kvs.ContainsKey("BatteryPath") Then
		BatteryLoc = Starter.kvs.Get("BatteryPath")
	End If
	
	If Starter.kvs.ContainsKey("VolLocation") Then
		VoltFile = Starter.kvs.Get("VolLocation")
	End If
	
	If Starter.kvs.ContainsKey("VolServiceDiv") Then
		VoltDiv = Starter.kvs.Get("VolServiceDiv")
	End If
	
	ShowNotif = True
End Sub

Sub Service_Start (StartingIntent As Intent)
	If StopNotif = False Then
		If IsScreenOn = True Then
			StartServiceAt(Me, DateTime.Now + 1, True)
			Log("screen on")
		Else
			StartServiceAt(Me, DateTime.Now + 60000, True)
			Log("screen off")
		End If
		Voltage_slow
		suloop
		
	End If
	If StopNotif = True Then
		If n.IsInitialized Then
			n.OnGoing(False)
		End If
		no.Cancel(0)
		no.Cancel(2) 
	End If
	
	If Starter.kvs.ContainsKey("VolServiceDiv") Then
		VoltDiv = Starter.kvs.Get("VolServiceDiv")
	End If
	
End Sub

Sub Voltage_slow
	
	Dim smiley As Bitmap
	Dim volts As String = File.ReadString(BatteryLoc, VoltFile)
	Dim volttext As Int = volts/VoltDiv
	Select Case volttext
		Case 44
			smiley = LoadBitmap(File.DirAssets, "44.png")
		Case 43
			smiley = LoadBitmap(File.DirAssets, "43.png")
		Case 42
			smiley = LoadBitmap(File.DirAssets, "42.png")
		Case 41
			smiley = LoadBitmap(File.DirAssets, "41.png")
		Case 40
			smiley = LoadBitmap(File.DirAssets, "40.png")
		Case 39
			smiley = LoadBitmap(File.DirAssets, "39.png")
		Case 38
			smiley = LoadBitmap(File.DirAssets, "38.png")
		Case 37
			smiley = LoadBitmap(File.DirAssets, "37.png")
		Case 36
			smiley = LoadBitmap(File.DirAssets, "36.png")
		Case 35
			smiley = LoadBitmap(File.DirAssets, "35.png")
		Case 34
			smiley = LoadBitmap(File.DirAssets, "34.png")
		Case 33
			smiley = LoadBitmap(File.DirAssets, "33.png")
		Case 32
			smiley = LoadBitmap(File.DirAssets, "32.png")
		Case 31
			smiley = LoadBitmap(File.DirAssets, "31.png")
		Case 30
			smiley = LoadBitmap(File.DirAssets, "30.png")
		Case 29
			smiley = LoadBitmap(File.DirAssets, "29.png")
		Case 28
			smiley = LoadBitmap(File.DirAssets, "28.png")
		Case 27
			smiley = LoadBitmap(File.DirAssets, "27.png")
		Case 26
			smiley = LoadBitmap(File.DirAssets, "26.png")
	End Select
	
	'if volt isn't correct
	If volttext < 26 Or volttext > 44 Then
		smiley = LoadBitmap(File.DirAssets, "warn.png")
		n.Initialize("default", Application.LabelName, "DEFAULT").AutoCancel(False).SmallIcon(smiley)
		n.Build("Voltage Not Correct", "Please open the app and set the correct voltage in the Adjust menu", "tag1", Me).Notify(1)
		n.BadgeIconType("LARGE")
		StopNotif = True
		Me
		ShowNotif = False
		Starter.kvs.put("ShowNotif", False)
		
		'if dumpsys is on then make it not
		If Starter.kvs.get("MockBatt") Then
		Starter.kvs.Put("MockBatt", False)
		Log(su.Execute("dumpsys battery reset"))
		End If
		
	End If
	
	If ShowNotif = True Then
		n.Initialize("default", Application.LabelName, "LOW").AutoCancel(False).SmallIcon(smiley)
	n.SetDefaults(False,False,False)
	n.OnGoing(True)
		n.Build("Battery Voltage : " & volts, "Reading From " & Starter.kvs.Get("BatteryPath") & Starter.kvs.Get("VolLocation"), "tag1", Me).Notify(0)
	n.BadgeIconType("LARGE")
	End If
End Sub


Sub Service_Destroy

End Sub

Sub suloop
	If Starter.kvs.get("MockBatt") = True And loopongoing = False Then
		Dim stri As String
		Dim str As Int
		Dim voldif As Int
		'voltage/needed division to reach two digit number - lowest voltage (0%) / voltage difference of 100% and 0% * 100
		'stri = ((((File.ReadString("/sys/class/power_supply/battery/", Starter.kvs.Get("VolLocation"))/Starter.kvs.Get("VolServiceDiv"))-30)/12)*100)
		voldif = ( Starter.kvs.Get("HundredVol") - Starter.kvs.Get("ZeroVol") )
		stri = ((((File.ReadString(BatteryLoc, Starter.kvs.Get("VolLocation"))/Starter.kvs.Get("VolServiceDiv"))-Starter.kvs.Get("ZeroVol"))/voldif)*100)
		str = stri
		'Log(su.Execute("dumpsys battery reset"))
		
		'making sure phone never turns of cause of 0%
		If str = 0 Or str < 0 Then
			Log(su.Execute("dumpsys " & Starter.kvs.Get("BatteryServiceName") & " set level 1"))
		Else
			Log(su.Execute("dumpsys " & Starter.kvs.Get("BatteryServiceName") & " set level " & str))
		End If
		
		If File.Exists(BatteryLoc, "capacity") Then
			Dim realp As Int = File.ReadString(BatteryLoc, "capacity")
			n.Initialize("default", Application.LabelName, "LOW").AutoCancel(False).SmallIcon(LoadBitmap(File.DirAssets, "fakebattery.png"))
			n.SetDefaults(False,False,False)
			n.Build("Battery Mocked To " & str & "%", realp & "% System Reported Percentage", "tag2", Me).Notify(2)
		Else
			n.Initialize("default", Application.LabelName, "LOW").AutoCancel(False).SmallIcon(LoadBitmap(File.DirAssets, "fakebattery.png"))
			n.SetDefaults(False,False,False)
			n.Build("Battery Mocked To " & str & "%", "", "tag2", Me).Notify(2)
		End If
		loopongoing = True
		Sleep(10000)
		loopongoing = False
	End If
End Sub

Sub IsScreenOn As Boolean
	Dim p As Phone
	If p.SdkVersion < 20 Then Return True 'not worth bothering with Android 4 devices
	Dim ctxt As JavaObject
	ctxt.InitializeContext
	Dim displays() As Object = ctxt.RunMethodJO("getSystemService", Array("display")).RunMethod("getDisplays", Null)
	For Each display As JavaObject In displays
		If display.RunMethod("getState", Null) <> 1 Then Return True '1 = Display.STATE_OFF
	Next
	Return False
End Sub