using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Whiteboard_Communication_Service.Event_Viewer_Integration
{
    class EventViewer
    {
        public static void logEvent(string errorMessage, EventLogEntryType errorType)
        {
            using (EventLog applicationLog = new EventLog("Application"))
            {
                applicationLog.Source = "Application";
                applicationLog.WriteEntry(errorMessage, errorType);
            }
        }
    }
}
