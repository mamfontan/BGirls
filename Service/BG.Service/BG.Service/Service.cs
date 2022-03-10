using BG.Service.model;
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

        private string _urlBase = "https://www.kindgirls.com/photo-archive.php?s={month}-{year}";

        private Timer _timer;

        private Gallery[] _galleries;

        public Service()
        {
            InitializeComponent();
        }

        protected override void OnStart(string[] args)
        {
            ReadGalleries();

            SetTimer();
        }

        protected override void OnStop()
        {
        }

        #region Private methods

        private void ReadGalleries()
        {
            _galleries = ReadGalleriesFromDisk();
            if (_galleries != null && _galleries.Count() == 0)
            {
                _galleries = LoadDefaultGalleries();
            }
        }

        private Gallery[] ReadGalleriesFromDisk()
        {
            _galleries = Array.Empty<Gallery>();

            return _galleries;
        }

        private Gallery[] LoadDefaultGalleries()
        {
            var data = new Gallery[] {
            };

            return data;
        }

        private void SetTimer()
        {
            _timer = new Timer();
            _timer.Interval = 1000 * 60 * 60;
            _timer.Start();
            _timer.Elapsed += OnTimedEvent;
        }

        private void OnTimedEvent(Object source, System.Timers.ElapsedEventArgs e)
        {
            var strYear = _actualTime.Year.ToString("####");
            var strMonth = _actualTime.Month.ToString("##");

        }
        #endregion
    }
}