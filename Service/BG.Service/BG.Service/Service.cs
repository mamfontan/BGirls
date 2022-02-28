using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics;
using System.Linq;
using System.ServiceProcess;
using System.Text;
using System.Threading.Tasks;
using System.Timers;

namespace BG.Service
{
    public partial class Service : ServiceBase
    {
        private DateTime _actualTime = DateTime.Now;

        private string _urlBase = "https://www.kindgirls.com/photo-archive.php?s=02-2022";

        private Timer _timer;

        public Service()
        {
            InitializeComponent();
        }

        protected override void OnStart(string[] args)
        {
            _timer = new Timer();
            _timer.Interval = 1000 * 60 * 60;
            _timer.Start();
            _timer.Elapsed += OnTimedEvent;
        }

        protected override void OnStop()
        {
        }

        #region Private methods
        private void OnTimedEvent(Object source, System.Timers.ElapsedEventArgs e)
        {
            var strYear = _actualTime.Year.ToString("####");
            var strMonth = _actualTime.Month.ToString("##");

        }
        #endregion
    }
}
