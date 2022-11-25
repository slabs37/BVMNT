package slabs37.batt.volt;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.ServiceHelper;
import anywheresoftware.b4a.debug.*;

public class voltage_notif extends  android.app.Service{
	public static class voltage_notif_BR extends android.content.BroadcastReceiver {

		@Override
		public void onReceive(android.content.Context context, android.content.Intent intent) {
            BA.LogInfo("** Receiver (voltage_notif) OnReceive **");
			android.content.Intent in = new android.content.Intent(context, voltage_notif.class);
			if (intent != null)
				in.putExtra("b4a_internal_intent", intent);
            ServiceHelper.StarterHelper.startServiceFromReceiver (context, in, false, BA.class);
		}

	}
    static voltage_notif mostCurrent;
	public static BA processBA;
    private ServiceHelper _service;
    public static Class<?> getObject() {
		return voltage_notif.class;
	}
	@Override
	public void onCreate() {
        super.onCreate();
        mostCurrent = this;
        if (processBA == null) {
		    processBA = new BA(this, null, null, "slabs37.batt.volt", "slabs37.batt.volt.voltage_notif");
            if (BA.isShellModeRuntimeCheck(processBA)) {
                processBA.raiseEvent2(null, true, "SHELL", false);
		    }
            try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            processBA.loadHtSubs(this.getClass());
            ServiceHelper.init();
        }
        _service = new ServiceHelper(this);
        processBA.service = this;
        
        if (BA.isShellModeRuntimeCheck(processBA)) {
			processBA.raiseEvent2(null, true, "CREATE", true, "slabs37.batt.volt.voltage_notif", processBA, _service, anywheresoftware.b4a.keywords.Common.Density);
		}
        if (!false && ServiceHelper.StarterHelper.startFromServiceCreate(processBA, false) == false) {
				
		}
		else {
            processBA.setActivityPaused(false);
            BA.LogInfo("*** Service (voltage_notif) Create ***");
            processBA.raiseEvent(null, "service_create");
        }
        processBA.runHook("oncreate", this, null);
        if (false) {
			ServiceHelper.StarterHelper.runWaitForLayouts();
		}
    }
		@Override
	public void onStart(android.content.Intent intent, int startId) {
		onStartCommand(intent, 0, 0);
    }
    @Override
    public int onStartCommand(final android.content.Intent intent, int flags, int startId) {
    	if (ServiceHelper.StarterHelper.onStartCommand(processBA, new Runnable() {
            public void run() {
                handleStart(intent);
            }}))
			;
		else {
			ServiceHelper.StarterHelper.addWaitForLayout (new Runnable() {
				public void run() {
                    processBA.setActivityPaused(false);
                    BA.LogInfo("** Service (voltage_notif) Create **");
                    processBA.raiseEvent(null, "service_create");
					handleStart(intent);
                    ServiceHelper.StarterHelper.removeWaitForLayout();
				}
			});
		}
        processBA.runHook("onstartcommand", this, new Object[] {intent, flags, startId});
		return android.app.Service.START_NOT_STICKY;
    }
    public void onTaskRemoved(android.content.Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        if (false)
            processBA.raiseEvent(null, "service_taskremoved");
            
    }
    private void handleStart(android.content.Intent intent) {
    	BA.LogInfo("** Service (voltage_notif) Start **");
    	java.lang.reflect.Method startEvent = processBA.htSubs.get("service_start");
    	if (startEvent != null) {
    		if (startEvent.getParameterTypes().length > 0) {
    			anywheresoftware.b4a.objects.IntentWrapper iw = ServiceHelper.StarterHelper.handleStartIntent(intent, _service, processBA);
    			processBA.raiseEvent(null, "service_start", iw);
    		}
    		else {
    			processBA.raiseEvent(null, "service_start");
    		}
    	}
    }
	
	@Override
	public void onDestroy() {
        super.onDestroy();
        if (false) {
            BA.LogInfo("** Service (voltage_notif) Destroy (ignored)**");
        }
        else {
            BA.LogInfo("** Service (voltage_notif) Destroy **");
		    processBA.raiseEvent(null, "service_destroy");
            processBA.service = null;
		    mostCurrent = null;
		    processBA.setActivityPaused(true);
            processBA.runHook("ondestroy", this, null);
        }
	}

@Override
	public android.os.IBinder onBind(android.content.Intent intent) {
		return null;
	}public anywheresoftware.b4a.keywords.Common __c = null;
public static boolean _stopnotif = false;
public static slabs37.batt.volt.nb6 _n = null;
public static anywheresoftware.b4a.objects.NotificationWrapper _no = null;
public static String _voltfile = "";
public static String _batteryloc = "";
public static boolean _shownotif = false;
public static int _voltdiv = 0;
public static com.datasteam.b4a.system.superuser.SuShell _su = null;
public static boolean _loopongoing = false;
public slabs37.batt.volt.main _main = null;
public slabs37.batt.volt.adjust _adjust = null;
public slabs37.batt.volt.starter _starter = null;
public slabs37.batt.volt.about _about = null;
public static boolean  _isscreenon() throws Exception{
anywheresoftware.b4a.phone.Phone _p = null;
anywheresoftware.b4j.object.JavaObject _ctxt = null;
Object[] _displays = null;
anywheresoftware.b4j.object.JavaObject _display = null;
 //BA.debugLineNum = 180;BA.debugLine="Sub IsScreenOn As Boolean";
 //BA.debugLineNum = 181;BA.debugLine="Dim p As Phone";
_p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 182;BA.debugLine="If p.SdkVersion < 20 Then Return True 'not worth";
if (_p.getSdkVersion()<20) { 
if (true) return anywheresoftware.b4a.keywords.Common.True;};
 //BA.debugLineNum = 183;BA.debugLine="Dim ctxt As JavaObject";
_ctxt = new anywheresoftware.b4j.object.JavaObject();
 //BA.debugLineNum = 184;BA.debugLine="ctxt.InitializeContext";
_ctxt.InitializeContext(processBA);
 //BA.debugLineNum = 185;BA.debugLine="Dim displays() As Object = ctxt.RunMethodJO(\"getS";
_displays = (Object[])(_ctxt.RunMethodJO("getSystemService",new Object[]{(Object)("display")}).RunMethod("getDisplays",(Object[])(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 186;BA.debugLine="For Each display As JavaObject In displays";
_display = new anywheresoftware.b4j.object.JavaObject();
{
final Object[] group6 = _displays;
final int groupLen6 = group6.length
;int index6 = 0;
;
for (; index6 < groupLen6;index6++){
_display = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(group6[index6]));
 //BA.debugLineNum = 187;BA.debugLine="If display.RunMethod(\"getState\", Null) <> 1 Then";
if ((_display.RunMethod("getState",(Object[])(anywheresoftware.b4a.keywords.Common.Null))).equals((Object)(1)) == false) { 
if (true) return anywheresoftware.b4a.keywords.Common.True;};
 }
};
 //BA.debugLineNum = 189;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 190;BA.debugLine="End Sub";
return false;
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim StopNotif As Boolean";
_stopnotif = false;
 //BA.debugLineNum = 10;BA.debugLine="Dim n As NB6";
_n = new slabs37.batt.volt.nb6();
 //BA.debugLineNum = 11;BA.debugLine="Dim no As Notification";
_no = new anywheresoftware.b4a.objects.NotificationWrapper();
 //BA.debugLineNum = 12;BA.debugLine="Dim VoltFile As String";
_voltfile = "";
 //BA.debugLineNum = 13;BA.debugLine="Dim BatteryLoc As String";
_batteryloc = "";
 //BA.debugLineNum = 14;BA.debugLine="Dim ShowNotif As Boolean";
_shownotif = false;
 //BA.debugLineNum = 15;BA.debugLine="Dim VoltDiv As Int";
_voltdiv = 0;
 //BA.debugLineNum = 16;BA.debugLine="Dim su As SuShell";
_su = new com.datasteam.b4a.system.superuser.SuShell();
 //BA.debugLineNum = 17;BA.debugLine="Dim loopongoing As Boolean";
_loopongoing = false;
 //BA.debugLineNum = 19;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
 //BA.debugLineNum = 21;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 22;BA.debugLine="Service.AutomaticForegroundMode = Service.AUTOMAT";
mostCurrent._service.AutomaticForegroundMode = mostCurrent._service.AUTOMATIC_FOREGROUND_NEVER;
 //BA.debugLineNum = 24;BA.debugLine="If Starter.kvs.ContainsKey(\"BatteryPath\") Then";
if (mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("BatteryPath")) { 
 //BA.debugLineNum = 25;BA.debugLine="BatteryLoc = Starter.kvs.Get(\"BatteryPath\")";
_batteryloc = BA.ObjectToString(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("BatteryPath"));
 };
 //BA.debugLineNum = 28;BA.debugLine="If Starter.kvs.ContainsKey(\"VolLocation\") Then";
if (mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("VolLocation")) { 
 //BA.debugLineNum = 29;BA.debugLine="VoltFile = Starter.kvs.Get(\"VolLocation\")";
_voltfile = BA.ObjectToString(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("VolLocation"));
 };
 //BA.debugLineNum = 32;BA.debugLine="If Starter.kvs.ContainsKey(\"VolServiceDiv\") Then";
if (mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("VolServiceDiv")) { 
 //BA.debugLineNum = 33;BA.debugLine="VoltDiv = Starter.kvs.Get(\"VolServiceDiv\")";
_voltdiv = (int)(BA.ObjectToNumber(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("VolServiceDiv")));
 };
 //BA.debugLineNum = 36;BA.debugLine="ShowNotif = True";
_shownotif = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 37;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 141;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 143;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
 //BA.debugLineNum = 39;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 40;BA.debugLine="If StopNotif = False Then";
if (_stopnotif==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 41;BA.debugLine="If IsScreenOn = True Then";
if (_isscreenon()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 42;BA.debugLine="StartServiceAt(Me, DateTime.Now + 1, True)";
anywheresoftware.b4a.keywords.Common.StartServiceAt(processBA,voltage_notif.getObject(),(long) (anywheresoftware.b4a.keywords.Common.DateTime.getNow()+1),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 43;BA.debugLine="Log(\"screen on\")";
anywheresoftware.b4a.keywords.Common.LogImpl("43080196","screen on",0);
 }else {
 //BA.debugLineNum = 45;BA.debugLine="StartServiceAt(Me, DateTime.Now + 60000, True)";
anywheresoftware.b4a.keywords.Common.StartServiceAt(processBA,voltage_notif.getObject(),(long) (anywheresoftware.b4a.keywords.Common.DateTime.getNow()+60000),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 46;BA.debugLine="Log(\"screen off\")";
anywheresoftware.b4a.keywords.Common.LogImpl("43080199","screen off",0);
 };
 //BA.debugLineNum = 48;BA.debugLine="Voltage_slow";
_voltage_slow();
 //BA.debugLineNum = 49;BA.debugLine="suloop";
_suloop();
 };
 //BA.debugLineNum = 52;BA.debugLine="If StopNotif = True Then";
if (_stopnotif==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 53;BA.debugLine="If n.IsInitialized Then";
if (_n.IsInitialized /*boolean*/ ()) { 
 //BA.debugLineNum = 54;BA.debugLine="n.OnGoing(False)";
_n._ongoing /*slabs37.batt.volt.nb6*/ (anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 56;BA.debugLine="no.Cancel(0)";
_no.Cancel((int) (0));
 //BA.debugLineNum = 57;BA.debugLine="no.Cancel(2)";
_no.Cancel((int) (2));
 };
 //BA.debugLineNum = 60;BA.debugLine="If Starter.kvs.ContainsKey(\"VolServiceDiv\") Then";
if (mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._containskey("VolServiceDiv")) { 
 //BA.debugLineNum = 61;BA.debugLine="VoltDiv = Starter.kvs.Get(\"VolServiceDiv\")";
_voltdiv = (int)(BA.ObjectToNumber(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("VolServiceDiv")));
 };
 //BA.debugLineNum = 64;BA.debugLine="End Sub";
return "";
}
public static void  _suloop() throws Exception{
ResumableSub_suloop rsub = new ResumableSub_suloop(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_suloop extends BA.ResumableSub {
public ResumableSub_suloop(slabs37.batt.volt.voltage_notif parent) {
this.parent = parent;
}
slabs37.batt.volt.voltage_notif parent;
String _stri = "";
int _str = 0;
int _voldif = 0;
int _realp = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 146;BA.debugLine="If Starter.kvs.get(\"MockBatt\") = True And loopong";
if (true) break;

case 1:
//if
this.state = 15;
if ((parent.mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("MockBatt")).equals((Object)(anywheresoftware.b4a.keywords.Common.True)) && parent._loopongoing==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 147;BA.debugLine="Dim stri As String";
_stri = "";
 //BA.debugLineNum = 148;BA.debugLine="Dim str As Int";
_str = 0;
 //BA.debugLineNum = 149;BA.debugLine="Dim voldif As Int";
_voldif = 0;
 //BA.debugLineNum = 152;BA.debugLine="voldif = ( Starter.kvs.Get(\"HundredVol\") - Start";
_voldif = (int) (((double)(BA.ObjectToNumber(parent.mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("HundredVol")))-(double)(BA.ObjectToNumber(parent.mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("ZeroVol")))));
 //BA.debugLineNum = 153;BA.debugLine="stri = ((((File.ReadString(BatteryLoc, Starter.k";
_stri = BA.NumberToString((((((double)(Double.parseDouble(anywheresoftware.b4a.keywords.Common.File.ReadString(parent._batteryloc,BA.ObjectToString(parent.mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("VolLocation")))))/(double)(double)(BA.ObjectToNumber(parent.mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("VolServiceDiv"))))-(double)(BA.ObjectToNumber(parent.mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("ZeroVol"))))/(double)_voldif)*100));
 //BA.debugLineNum = 154;BA.debugLine="str = stri";
_str = (int)(Double.parseDouble(_stri));
 //BA.debugLineNum = 158;BA.debugLine="If str = 0 Or str < 0 Then";
if (true) break;

case 4:
//if
this.state = 9;
if (_str==0 || _str<0) { 
this.state = 6;
}else {
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 9;
 //BA.debugLineNum = 159;BA.debugLine="Log(su.Execute(\"dumpsys \" & Starter.kvs.Get(\"Ba";
anywheresoftware.b4a.keywords.Common.LogImpl("43276814",BA.ObjectToString(parent._su.Execute(processBA,"dumpsys "+BA.ObjectToString(parent.mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("BatteryServiceName"))+" set level 1")),0);
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 161;BA.debugLine="Log(su.Execute(\"dumpsys \" & Starter.kvs.Get(\"Ba";
anywheresoftware.b4a.keywords.Common.LogImpl("43276816",BA.ObjectToString(parent._su.Execute(processBA,"dumpsys "+BA.ObjectToString(parent.mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("BatteryServiceName"))+" set level "+BA.NumberToString(_str))),0);
 if (true) break;
;
 //BA.debugLineNum = 164;BA.debugLine="If File.Exists(BatteryLoc, \"capacity\") Then";

case 9:
//if
this.state = 14;
if (anywheresoftware.b4a.keywords.Common.File.Exists(parent._batteryloc,"capacity")) { 
this.state = 11;
}else {
this.state = 13;
}if (true) break;

case 11:
//C
this.state = 14;
 //BA.debugLineNum = 165;BA.debugLine="Dim realp As Int = File.ReadString(BatteryLoc,";
_realp = (int)(Double.parseDouble(anywheresoftware.b4a.keywords.Common.File.ReadString(parent._batteryloc,"capacity")));
 //BA.debugLineNum = 166;BA.debugLine="n.Initialize(\"default\", Application.LabelName,";
parent._n._initialize /*slabs37.batt.volt.nb6*/ (processBA,"default",(Object)(anywheresoftware.b4a.keywords.Common.Application.getLabelName()),"LOW")._autocancel /*slabs37.batt.volt.nb6*/ (anywheresoftware.b4a.keywords.Common.False)._smallicon /*slabs37.batt.volt.nb6*/ (anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"fakebattery.png"));
 //BA.debugLineNum = 167;BA.debugLine="n.SetDefaults(False,False,False)";
parent._n._setdefaults /*slabs37.batt.volt.nb6*/ (anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 168;BA.debugLine="n.Build(\"Battery Mocked To \" & str & \"%\", realp";
parent._n._build /*anywheresoftware.b4a.objects.NotificationWrapper*/ ((Object)("Battery Mocked To "+BA.NumberToString(_str)+"%"),(Object)(BA.NumberToString(_realp)+"% System Reported Percentage"),"tag2",voltage_notif.getObject()).Notify((int) (2));
 if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 170;BA.debugLine="n.Initialize(\"default\", Application.LabelName,";
parent._n._initialize /*slabs37.batt.volt.nb6*/ (processBA,"default",(Object)(anywheresoftware.b4a.keywords.Common.Application.getLabelName()),"LOW")._autocancel /*slabs37.batt.volt.nb6*/ (anywheresoftware.b4a.keywords.Common.False)._smallicon /*slabs37.batt.volt.nb6*/ (anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"fakebattery.png"));
 //BA.debugLineNum = 171;BA.debugLine="n.SetDefaults(False,False,False)";
parent._n._setdefaults /*slabs37.batt.volt.nb6*/ (anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 172;BA.debugLine="n.Build(\"Battery Mocked To \" & str & \"%\", \"\", \"";
parent._n._build /*anywheresoftware.b4a.objects.NotificationWrapper*/ ((Object)("Battery Mocked To "+BA.NumberToString(_str)+"%"),(Object)(""),"tag2",voltage_notif.getObject()).Notify((int) (2));
 if (true) break;

case 14:
//C
this.state = 15;
;
 //BA.debugLineNum = 174;BA.debugLine="loopongoing = True";
parent._loopongoing = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 175;BA.debugLine="Sleep(10000)";
anywheresoftware.b4a.keywords.Common.Sleep(processBA,this,(int) (10000));
this.state = 16;
return;
case 16:
//C
this.state = 15;
;
 //BA.debugLineNum = 176;BA.debugLine="loopongoing = False";
parent._loopongoing = anywheresoftware.b4a.keywords.Common.False;
 if (true) break;

case 15:
//C
this.state = -1;
;
 //BA.debugLineNum = 178;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _voltage_slow() throws Exception{
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _smiley = null;
String _volts = "";
int _volttext = 0;
 //BA.debugLineNum = 66;BA.debugLine="Sub Voltage_slow";
 //BA.debugLineNum = 68;BA.debugLine="Dim smiley As Bitmap";
_smiley = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 69;BA.debugLine="Dim volts As String = File.ReadString(BatteryLoc,";
_volts = anywheresoftware.b4a.keywords.Common.File.ReadString(_batteryloc,_voltfile);
 //BA.debugLineNum = 70;BA.debugLine="Dim volttext As Int = volts/VoltDiv";
_volttext = (int) ((double)(Double.parseDouble(_volts))/(double)_voltdiv);
 //BA.debugLineNum = 71;BA.debugLine="Select Case volttext";
switch (_volttext) {
case 44: {
 //BA.debugLineNum = 73;BA.debugLine="smiley = LoadBitmap(File.DirAssets, \"44.png\")";
_smiley = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"44.png");
 break; }
case 43: {
 //BA.debugLineNum = 75;BA.debugLine="smiley = LoadBitmap(File.DirAssets, \"43.png\")";
_smiley = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"43.png");
 break; }
case 42: {
 //BA.debugLineNum = 77;BA.debugLine="smiley = LoadBitmap(File.DirAssets, \"42.png\")";
_smiley = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"42.png");
 break; }
case 41: {
 //BA.debugLineNum = 79;BA.debugLine="smiley = LoadBitmap(File.DirAssets, \"41.png\")";
_smiley = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"41.png");
 break; }
case 40: {
 //BA.debugLineNum = 81;BA.debugLine="smiley = LoadBitmap(File.DirAssets, \"40.png\")";
_smiley = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"40.png");
 break; }
case 39: {
 //BA.debugLineNum = 83;BA.debugLine="smiley = LoadBitmap(File.DirAssets, \"39.png\")";
_smiley = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"39.png");
 break; }
case 38: {
 //BA.debugLineNum = 85;BA.debugLine="smiley = LoadBitmap(File.DirAssets, \"38.png\")";
_smiley = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"38.png");
 break; }
case 37: {
 //BA.debugLineNum = 87;BA.debugLine="smiley = LoadBitmap(File.DirAssets, \"37.png\")";
_smiley = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"37.png");
 break; }
case 36: {
 //BA.debugLineNum = 89;BA.debugLine="smiley = LoadBitmap(File.DirAssets, \"36.png\")";
_smiley = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"36.png");
 break; }
case 35: {
 //BA.debugLineNum = 91;BA.debugLine="smiley = LoadBitmap(File.DirAssets, \"35.png\")";
_smiley = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"35.png");
 break; }
case 34: {
 //BA.debugLineNum = 93;BA.debugLine="smiley = LoadBitmap(File.DirAssets, \"34.png\")";
_smiley = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"34.png");
 break; }
case 33: {
 //BA.debugLineNum = 95;BA.debugLine="smiley = LoadBitmap(File.DirAssets, \"33.png\")";
_smiley = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"33.png");
 break; }
case 32: {
 //BA.debugLineNum = 97;BA.debugLine="smiley = LoadBitmap(File.DirAssets, \"32.png\")";
_smiley = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"32.png");
 break; }
case 31: {
 //BA.debugLineNum = 99;BA.debugLine="smiley = LoadBitmap(File.DirAssets, \"31.png\")";
_smiley = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"31.png");
 break; }
case 30: {
 //BA.debugLineNum = 101;BA.debugLine="smiley = LoadBitmap(File.DirAssets, \"30.png\")";
_smiley = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"30.png");
 break; }
case 29: {
 //BA.debugLineNum = 103;BA.debugLine="smiley = LoadBitmap(File.DirAssets, \"29.png\")";
_smiley = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"29.png");
 break; }
case 28: {
 //BA.debugLineNum = 105;BA.debugLine="smiley = LoadBitmap(File.DirAssets, \"28.png\")";
_smiley = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"28.png");
 break; }
case 27: {
 //BA.debugLineNum = 107;BA.debugLine="smiley = LoadBitmap(File.DirAssets, \"27.png\")";
_smiley = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"27.png");
 break; }
case 26: {
 //BA.debugLineNum = 109;BA.debugLine="smiley = LoadBitmap(File.DirAssets, \"26.png\")";
_smiley = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"26.png");
 break; }
}
;
 //BA.debugLineNum = 113;BA.debugLine="If volttext < 26 Or volttext > 44 Then";
if (_volttext<26 || _volttext>44) { 
 //BA.debugLineNum = 114;BA.debugLine="smiley = LoadBitmap(File.DirAssets, \"warn.png\")";
_smiley = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"warn.png");
 //BA.debugLineNum = 115;BA.debugLine="n.Initialize(\"default\", Application.LabelName, \"";
_n._initialize /*slabs37.batt.volt.nb6*/ (processBA,"default",(Object)(anywheresoftware.b4a.keywords.Common.Application.getLabelName()),"DEFAULT")._autocancel /*slabs37.batt.volt.nb6*/ (anywheresoftware.b4a.keywords.Common.False)._smallicon /*slabs37.batt.volt.nb6*/ (_smiley);
 //BA.debugLineNum = 116;BA.debugLine="n.Build(\"Voltage Not Correct\", \"Please open the";
_n._build /*anywheresoftware.b4a.objects.NotificationWrapper*/ ((Object)("Voltage Not Correct"),(Object)("Please open the app and set the correct voltage in the Adjust menu"),"tag1",voltage_notif.getObject()).Notify((int) (1));
 //BA.debugLineNum = 117;BA.debugLine="n.BadgeIconType(\"LARGE\")";
_n._badgeicontype /*slabs37.batt.volt.nb6*/ ("LARGE");
 //BA.debugLineNum = 118;BA.debugLine="StopNotif = True";
_stopnotif = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 119;BA.debugLine="Me";
voltage_notif.getObject();
 //BA.debugLineNum = 120;BA.debugLine="ShowNotif = False";
_shownotif = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 121;BA.debugLine="Starter.kvs.put(\"ShowNotif\", False)";
mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._put("ShowNotif",(Object)(anywheresoftware.b4a.keywords.Common.False));
 //BA.debugLineNum = 124;BA.debugLine="If Starter.kvs.get(\"MockBatt\") Then";
if (BA.ObjectToBoolean(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("MockBatt"))) { 
 //BA.debugLineNum = 125;BA.debugLine="Starter.kvs.Put(\"MockBatt\", False)";
mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._put("MockBatt",(Object)(anywheresoftware.b4a.keywords.Common.False));
 //BA.debugLineNum = 126;BA.debugLine="Log(su.Execute(\"dumpsys battery reset\"))";
anywheresoftware.b4a.keywords.Common.LogImpl("43145788",BA.ObjectToString(_su.Execute(processBA,"dumpsys battery reset")),0);
 };
 };
 //BA.debugLineNum = 131;BA.debugLine="If ShowNotif = True Then";
if (_shownotif==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 132;BA.debugLine="n.Initialize(\"default\", Application.LabelName, \"";
_n._initialize /*slabs37.batt.volt.nb6*/ (processBA,"default",(Object)(anywheresoftware.b4a.keywords.Common.Application.getLabelName()),"LOW")._autocancel /*slabs37.batt.volt.nb6*/ (anywheresoftware.b4a.keywords.Common.False)._smallicon /*slabs37.batt.volt.nb6*/ (_smiley);
 //BA.debugLineNum = 133;BA.debugLine="n.SetDefaults(False,False,False)";
_n._setdefaults /*slabs37.batt.volt.nb6*/ (anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 134;BA.debugLine="n.OnGoing(True)";
_n._ongoing /*slabs37.batt.volt.nb6*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 135;BA.debugLine="n.Build(\"Battery Voltage : \" & volts, \"Reading F";
_n._build /*anywheresoftware.b4a.objects.NotificationWrapper*/ ((Object)("Battery Voltage : "+_volts),(Object)("Reading From "+BA.ObjectToString(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("BatteryPath"))+BA.ObjectToString(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("VolLocation"))),"tag1",voltage_notif.getObject()).Notify((int) (0));
 //BA.debugLineNum = 136;BA.debugLine="n.BadgeIconType(\"LARGE\")";
_n._badgeicontype /*slabs37.batt.volt.nb6*/ ("LARGE");
 };
 //BA.debugLineNum = 138;BA.debugLine="End Sub";
return "";
}
}
