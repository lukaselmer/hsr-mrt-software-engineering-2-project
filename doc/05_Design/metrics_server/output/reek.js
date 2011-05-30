              var g = new Bluff.Line('graph', "1000x600");
      g.theme_37signals();
      g.tooltips = true;
      g.title_font_size = "24px"
      g.legend_font_size = "12px"
      g.marker_font_size = "10px"

        g.title = 'Reek: code smells';
        g.data('Duplication', [45])
g.data('LongMethod', [2])
g.data('LowCohesion', [5])
g.data('NestedIterators', [28])
g.data('SimulatedPolymorphism', [1])
g.data('UncommunicativeName', [13])

        g.labels = {"0":"5/30"};
        g.draw();
