(ns l_system_lab.student)

(defn remove-empties
  [v]
  (filter #(not= [] %) v))

(defn get-xy-scale
  "Get the scaling factor for the coordinates."
  ([v] (let [[_ x1 y1 x2 y2] (first v)] (get-xy-scale (rest v) (min x1 x2) (min y1 y2) (max x1 x2) (max y1 y2))))
  ([v min-x min-y max-x max-y]
    (if (empty? v) {:scale (/ 480.0 (max (- max-x min-x) (- max-y min-y))) :min-x min-x :min-y min-y}
      (let [[_ x1 y1 x2 y2] (first v)]
        (recur (rest v) 
               (min min-x (min x1 x2)) 
               (min min-y (min y1 y2)) 
               (max max-x (max x1 x2)) 
               (max max-y (max y1 y2))
        )))
))

(defn scale-turtle
  "Normalizes a list of [:line ... ] vectors."
  ([v] (scale-turtle v (get-xy-scale (remove-empties v)) []))
  ([v scale out]
  (if (empty? v) out
    (let [[_ x1 y1 x2 y2] (first v)
          scl (:scale scale)
          min-x (:min-x scale)
          min-y (:min-y scale)
          nx1 (+ 10 (* scl (- x1 min-x)))
          ny1 (+ 10 (* scl (- y1 min-y)))
          nx2 (+ 10 (* scl (- x2 min-x)))
          ny2 (+ 10 (* scl (- y2 min-y)))] 
      (recur (rest v) scale (conj out [:line nx1 ny1 nx2 ny2]))))
))

(defn transform
  [init-pat rules]
  (loop [v init-pat
         out []]
    (if (empty? v) out
      (if (not= nil (rules (first v)))
        (recur (rest v) (into out (rules (first v))))
        (recur (rest v) (conj out (first v)))))))
