## [Lindenmayer system](https://en.wikipedia.org/wiki/L-system) visualizer in Clojure

The curves defined in this program are of the form 
1. initial state, 
2. replacement rule, 
3. angle (for the angle change operator - or +)
4. initial angle


In code, the Koch curve would look like:
```clojure
;Koch curve
(def koch-curve
  {:init  [:f :- :- :f :- :- :f]
   :rules {:f [:f :+ :f :- :- :f :+ :f]}
   :theta 60
   :init-angle 0})
```

## Running the visualizer

It is assumed that you are using [lein](https://leiningen.org/) to build your Clojure. 

1. Download [Xming X Server](https://sourceforge.net/projects/xming/).
2. To test that Xming is working, run `sudo apt-get install x11-apps`, `export DISPLAY=:0`, and finally `xeyes`. 
  3. You'll know what xeyes is when you see it.
  4. Note: Though you won't always need to test whether Xming is working after the first time, you must run <br>`export DISPLAY=:0` every 
     new command line session for running this code.
5. Once you confirm that Xming is working, compile the code in `lein repl` with `(ns l_system_lab.core)` and <br>`(use 'l_system_lab.core :reload-all)`.
