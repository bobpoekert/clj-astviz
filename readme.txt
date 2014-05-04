Render graphs from clojure expressions.

Requires that you have graphviz installed (you should be able to get it from homebrew on mac).

Usage: java -jar standalone.jar <name of png file to write to> <clojure expression>

Exmaple: java -jar standalone.jar tree.png '(for [x (range 100)] {:x x})'
