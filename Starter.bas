B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Service
Version=9.9
@EndOfDesignText@
#Region  Service Attributes 
	#StartAtBoot: False
	#ExcludeFromLibrary: True
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Public kvs As KeyValueStore
	Public VolDiv As Int
	Public AmpDiv As Int
	Public VolServiceDiv As Int
	Public ZeroVol As Int
	Public HundredVol As Int
End Sub

Sub Service_Create
	'This is the program entry point.
	'This is a good place to load resources that are not specific to a single activity.
	kvs.Initialize(File.DirInternal, "datastore2")
End Sub

Sub Service_Start (StartingIntent As Intent)
	Service.StopAutomaticForeground 'Starter service can start in the foreground state in some edge cases.
End Sub


Sub Service_TaskRemoved
	'This event will be raised when the user removes the app from the recent apps list.
End Sub

'Return true to allow the OS default exceptions handler to handle the uncaught exception.
Sub Application_Error (Error As Exception, StackTrace As String) As Boolean
	Return True
End Sub

Sub Service_Destroy

End Sub

Sub CheckDivisions
	'check values and set if they dont exist
	If kvs.ContainsKey("VolDiv") Then
		VolDiv = kvs.Get("VolDiv")
	Else
		VolDiv = 100
		kvs.Put("VolDiv", VolDiv)
	End If
	
	If kvs.ContainsKey("AmpDiv") Then
		AmpDiv = kvs.Get("AmpDiv")
	Else
		AmpDiv = 1000
		kvs.Put("AmpDiv", AmpDiv)
	End If
	
	If kvs.ContainsKey("VolServiceDiv") Then
		VolServiceDiv = kvs.Get("VolServiceDiv")
	Else
		VolServiceDiv = 100000
		kvs.Put("VolServiceDiv", VolServiceDiv)
	End If
	
	If kvs.ContainsKey("BatteryServiceName") = False Then
		kvs.Put("BatteryServiceName", "battery")
	End If
	
	'check percentage requirements
	If kvs.ContainsKey("ZeroVol") Then
		ZeroVol = kvs.Get("ZeroVol")
	Else
		ZeroVol = 30
		kvs.Put("ZeroVol", ZeroVol)
	End If
	If kvs.ContainsKey("HundredVol") Then
		HundredVol = kvs.Get("HundredVol")
	Else
		HundredVol = 42
		kvs.Put("HundredVol", HundredVol)
	End If
End Sub
