package slabs37.batt.volt;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.ServiceHelper;
import anywheresoftware.b4a.debug.*;

public class starter extends  android.app.Service{
	public static class starter_BR extends android.content.BroadcastReceiver {

		@Override
		public void onReceive(android.content.Context context, android.content.Intent intent) {
            BA.LogInfo("** Receiver (starter) OnReceive **");
			android.content.Intent in = new android.content.Intent(context, starter.class);
			if (intent != null)
				in.putExtra("b4a_internal_intent", intent);
            ServiceHelper.StarterHelper.startServiceFromReceiver (context, in, true, BA.class);
		}

	}
    static starter mostCurrent;
	public static BA processBA;
    private ServiceHelper _service;
    public static Class<?> getObject() {
		return starter.class;
	}
	@Override
	public void onCreate() {
        super.onCreate();
        mostCurrent = this;
        if (processBA == null) {
		    processBA = new BA(this, null, null, "slabs37.batt.volt", "slabs37.batt.volt.starter");
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
			processBA.raiseEvent2(null, true, "CREATE", true, "slabs37.batt.volt.starter", processBA, _service, anywheresoftware.b4a.keywords.Common.Density);
		}
        if (!true && ServiceHelper.StarterHelper.startFromServiceCreate(processBA, false) == false) {
				
		}
		else {
            processBA.setActivityPaused(false);
            BA.LogInfo("*** Service (starter) Create ***");
            processBA.raiseEvent(null, "service_create");
        }
        processBA.runHook("oncreate", this, null);
        if (true) {
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
                    BA.LogInfo("** Service (starter) Create **");
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
        if (true)
            processBA.raiseEvent(null, "service_taskremoved");
            
    }
    private void handleStart(android.content.Intent intent) {
    	BA.LogInfo("** Service (starter) Start **");
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
        if (true) {
            BA.LogInfo("** Service (starter) Destroy (ignored)**");
        }
        else {
            BA.LogInfo("** Service (starter) Destroy **");
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
public static b4a.example3.keyvaluestore _kvs = null;
public static int _voldiv = 0;
public static int _ampdiv = 0;
public static int _volservicediv = 0;
public static int _zerovol = 0;
public static int _hundredvol = 0;
public slabs37.batt.volt.main _main = null;
public slabs37.batt.volt.adjust _adjust = null;
public slabs37.batt.volt.voltage_notif _voltage_notif = null;
public slabs37.batt.volt.about _about = null;
public static boolean  _application_error(anywheresoftware.b4a.objects.B4AException _error,String _stacktrace) throws Exception{
 //BA.debugLineNum = 33;BA.debugLine="Sub Application_Error (Error As Exception, StackTr";
 //BA.debugLineNum = 34;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 35;BA.debugLine="End Sub";
return false;
}
public static String  _checkdivisions() throws Exception{
 //BA.debugLineNum = 41;BA.debugLine="Sub CheckDivisions";
 //BA.debugLineNum = 43;BA.debugLine="If kvs.ContainsKey(\"VolDiv\") Then";
if (_kvs._containskey("VolDiv")) { 
 //BA.debugLineNum = 44;BA.debugLine="VolDiv = kvs.Get(\"VolDiv\")";
_voldiv = (int)(BA.ObjectToNumber(_kvs._get("VolDiv")));
 }else {
 //BA.debugLineNum = 46;BA.debugLine="VolDiv = 100";
_voldiv = (int) (100);
 //BA.debugLineNum = 47;BA.debugLine="kvs.Put(\"VolDiv\", VolDiv)";
_kvs._put("VolDiv",(Object)(_voldiv));
 };
 //BA.debugLineNum = 50;BA.debugLine="If kvs.ContainsKey(\"AmpDiv\") Then";
if (_kvs._containskey("AmpDiv")) { 
 //BA.debugLineNum = 51;BA.debugLine="AmpDiv = kvs.Get(\"AmpDiv\")";
_ampdiv = (int)(BA.ObjectToNumber(_kvs._get("AmpDiv")));
 }else {
 //BA.debugLineNum = 53;BA.debugLine="AmpDiv = 1000";
_ampdiv = (int) (1000);
 //BA.debugLineNum = 54;BA.debugLine="kvs.Put(\"AmpDiv\", AmpDiv)";
_kvs._put("AmpDiv",(Object)(_ampdiv));
 };
 //BA.debugLineNum = 57;BA.debugLine="If kvs.ContainsKey(\"VolServiceDiv\") Then";
if (_kvs._containskey("VolServiceDiv")) { 
 //BA.debugLineNum = 58;BA.debugLine="VolServiceDiv = kvs.Get(\"VolServiceDiv\")";
_volservicediv = (int)(BA.ObjectToNumber(_kvs._get("VolServiceDiv")));
 }else {
 //BA.debugLineNum = 60;BA.debugLine="VolServiceDiv = 100000";
_volservicediv = (int) (100000);
 //BA.debugLineNum = 61;BA.debugLine="kvs.Put(\"VolServiceDiv\", VolServiceDiv)";
_kvs._put("VolServiceDiv",(Object)(_volservicediv));
 };
 //BA.debugLineNum = 64;BA.debugLine="If kvs.ContainsKey(\"BatteryServiceName\") = False";
if (_kvs._containskey("BatteryServiceName")==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 65;BA.debugLine="kvs.Put(\"BatteryServiceName\", \"battery\")";
_kvs._put("BatteryServiceName",(Object)("battery"));
 };
 //BA.debugLineNum = 69;BA.debugLine="If kvs.ContainsKey(\"ZeroVol\") Then";
if (_kvs._containskey("ZeroVol")) { 
 //BA.debugLineNum = 70;BA.debugLine="ZeroVol = kvs.Get(\"ZeroVol\")";
_zerovol = (int)(BA.ObjectToNumber(_kvs._get("ZeroVol")));
 }else {
 //BA.debugLineNum = 72;BA.debugLine="ZeroVol = 30";
_zerovol = (int) (30);
 //BA.debugLineNum = 73;BA.debugLine="kvs.Put(\"ZeroVol\", ZeroVol)";
_kvs._put("ZeroVol",(Object)(_zerovol));
 };
 //BA.debugLineNum = 75;BA.debugLine="If kvs.ContainsKey(\"HundredVol\") Then";
if (_kvs._containskey("HundredVol")) { 
 //BA.debugLineNum = 76;BA.debugLine="HundredVol = kvs.Get(\"HundredVol\")";
_hundredvol = (int)(BA.ObjectToNumber(_kvs._get("HundredVol")));
 }else {
 //BA.debugLineNum = 78;BA.debugLine="HundredVol = 42";
_hundredvol = (int) (42);
 //BA.debugLineNum = 79;BA.debugLine="kvs.Put(\"HundredVol\", HundredVol)";
_kvs._put("HundredVol",(Object)(_hundredvol));
 };
 //BA.debugLineNum = 81;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Public kvs As KeyValueStore";
_kvs = new b4a.example3.keyvaluestore();
 //BA.debugLineNum = 10;BA.debugLine="Public VolDiv As Int";
_voldiv = 0;
 //BA.debugLineNum = 11;BA.debugLine="Public AmpDiv As Int";
_ampdiv = 0;
 //BA.debugLineNum = 12;BA.debugLine="Public VolServiceDiv As Int";
_volservicediv = 0;
 //BA.debugLineNum = 13;BA.debugLine="Public ZeroVol As Int";
_zerovol = 0;
 //BA.debugLineNum = 14;BA.debugLine="Public HundredVol As Int";
_hundredvol = 0;
 //BA.debugLineNum = 15;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
 //BA.debugLineNum = 17;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 20;BA.debugLine="kvs.Initialize(File.DirInternal, \"datastore2\")";
_kvs._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"datastore2");
 //BA.debugLineNum = 21;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 37;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 39;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
 //BA.debugLineNum = 23;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 24;BA.debugLine="Service.StopAutomaticForeground 'Starter service";
mostCurrent._service.StopAutomaticForeground();
 //BA.debugLineNum = 25;BA.debugLine="End Sub";
return "";
}
public static String  _service_taskremoved() throws Exception{
 //BA.debugLineNum = 28;BA.debugLine="Sub Service_TaskRemoved";
 //BA.debugLineNum = 30;BA.debugLine="End Sub";
return "";
}
}
