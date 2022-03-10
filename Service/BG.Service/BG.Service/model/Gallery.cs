namespace BG.Service.model
{
    internal class Gallery
    {
        public int Id { get; set; }

        public int Year { get; set; }

        public int Month { get; set; }

        public string Name { get; set; }

        public string Image { get; set; }

        public int Views { get; set; }

        public float Rating { get; set; }

        public string[] Pics { get; set; }

        public bool Saved { get; set; }
    }
}
