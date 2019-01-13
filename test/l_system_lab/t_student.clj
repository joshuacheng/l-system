(ns l_system_lab.t-student
  (:use midje.sweet)
  (:use [l_system_lab.student])
  (:use [l_system_lab.core]))

(facts "about transform"
       (fact "If there are no rules, it leaves the initial pattern alone."
             (transform [:f :x] {}) => [:f :x]
             (transform [:f :x] {:y [:a :b :c] :z [:a :b :c]}) => [:f :x])
       (fact "It changes things it has rules about."
             (transform [:f :x] {:x [:x :a :b]
                                 :f [:a :b :c]})
             => [:a :b :c :x :a :b]
             (transform [:f :x] {:x [:x :a :b]})
             => [:f :x :a :b]
             (transform [:f :x :f] {:f [:a :b :c]})
             => [:a :b :c :x :a :b :c]))

(defn rough-scale [expected]
  (fn [actual]
    (and ((roughly (:scale expected)) (:scale actual))
         (= (:min-x expected) (:min-x actual))
         (= (:min-y expected) (:min-y actual)))))

(facts "about get-xy-scale"
       (fact "It works when x and y have the same range."
             (get-xy-scale [[:line 10 10 20 20]])
             => (rough-scale {:scale 48.0 :min-x 10 :min-y 10})
             (get-xy-scale [[:line 10 10 20 20] [:line 50 90 90 50]])
             => (rough-scale {:scale 6.0 :min-x 10 :min-y 10})
             (get-xy-scale [[:line 50 90 20 10] [:line 10 20 90 50]])
             => (rough-scale {:scale 6.0 :min-x 10 :min-y 10}))
       (fact "It works when x has a larger range than y."
             (get-xy-scale [[:line 10 90 1000 95] [:line 2000 100 3000 110]])
             => (rough-scale {:scale  0.16053511 :min-x 10 :min-y 90}))
       (fact "It works when y has a larger range than x."
             (get-xy-scale [[:line 90 90 95 1000] [:line 100 2000 110 3000]])
             => (rough-scale {:scale  0.16494845, :min-x 90, :min-y 90})))

(defn rough-line [[_ e0 e1 e2 e3]]
  (fn [[_ a0 a1 a2 a3]]
    (and ((roughly e0) a0)
         ((roughly e1) a1)
         ((roughly e2) a2)
         ((roughly e3) a3))))

(defn rough-lines [expected]
  (fn [actual]
    (loop [e-stuff expected
           a-stuff actual]
      (cond (empty? a-stuff) true
            ((rough-line (first e-stuff))
             (first a-stuff))
            (recur (rest e-stuff)
                   (rest a-stuff))
            :else false))))

(facts "about scale-turtle"
       (fact "It works when x and y have the same range."
             (scale-turtle [[:line 10 10 20 20]])
             => (rough-lines [[:line 10.0 10.0 490.0 490.0]])
             (scale-turtle [[:line 10 10 20 20] [:line 50 90 90 50]])
             => (rough-lines [[:line 10.0 10.0 70.0 70.0] [:line 250.0 490.0 490.0 250.0]])
             (scale-turtle [[:line 50 90 20 10] [:line 10 20 90 50]])
             => (rough-lines [[:line 250.0 490.0 70.0 10.0] [:line 10.0 70.0 490.0 250.0]]))
       (fact "It works when x has a larger range than y."
             (scale-turtle [[:line 10 90 1000 95] [:line 2000 100 3000 110]])
             => (rough-lines [[:line 10.0 10.0 168.92976 10.802675] [:line 329.46487 11.605351 490.0 13.210702]] ))
       (fact "It works when y has a larger range than x."
             (scale-turtle [[:line 90 90 95 1000] [:line 100 2000 110 3000]])
             => (rough-lines [[:line 10.0 10.0 10.824742 160.10309] [:line 11.649485 325.05154 13.298969 490.0]] )))
