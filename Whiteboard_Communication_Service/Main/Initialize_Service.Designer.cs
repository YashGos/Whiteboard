namespace Whiteboard_Communication_Service
{
    partial class Initialize_Service
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

        // Design Service.
        private void InitializeComponent()
        {
            components = new System.ComponentModel.Container();
            this.ServiceName = "WBCom";
            this.CanPauseAndContinue = false;
        }
    }
}
