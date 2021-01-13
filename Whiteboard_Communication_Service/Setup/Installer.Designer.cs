namespace Whiteboard_Communication_Service
{
    partial class Installer
    {
        // Required designer variable.
        private System.ComponentModel.IContainer components = null;

        // Clean up any resources being used.
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        // Design Installer
        private void InitializeComponent()
        {
            this.serviceProcessInstaller = new System.ServiceProcess.ServiceProcessInstaller();
            this.serviceInstaller = new System.ServiceProcess.ServiceInstaller();

            this.serviceProcessInstaller.Account = System.ServiceProcess.ServiceAccount.LocalSystem;
            this.serviceProcessInstaller.Password = null;
            this.serviceProcessInstaller.Username = null;

            this.serviceInstaller.Description = "Establishes control interface between Whiteboard client and server resourcces. Handles authentication, DB access, and filesystem access.";
            this.serviceInstaller.DisplayName = "Whiteboard Communication Service";
            this.serviceInstaller.ServiceName = "WBCom";
            this.serviceInstaller.StartType = System.ServiceProcess.ServiceStartMode.Automatic;
            //Fix this later.....
            //this.serviceInstaller.ServicesDependedOn = ;

            this.Installers.AddRange(new System.Configuration.Install.Installer[] {this.serviceProcessInstaller,this.serviceInstaller});
        }
        private System.ServiceProcess.ServiceProcessInstaller serviceProcessInstaller;
        private System.ServiceProcess.ServiceInstaller serviceInstaller;
    }
}