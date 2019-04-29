(defproject liu.mars/akka-clojure "0.1.6"
  :description "akka toolkit for clojure programmer"
  :url "https://github.com/MarchLiu/akka-clojure"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :source-paths ["src/main/clojure"]
  :java-source-paths ["src/main/java"]
  :test-paths ["src/test/clojure"]
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [com.typesafe.akka/akka-actor_2.12 "2.5.19"]
                 [liu.mars/jaskell "0.2.2"]]
  :deploy-repositories [["releases" :clojars]
                        ["snapshots" :clojars]]
  :profiles {:test {:dependencies          [[com.typesafe.akka/akka-testkit_2.12 "2.5.19"]]
                    :plugins               [[lein-test-report-junit-xml "0.2.0"]]
                    :test-report-junit-xml {:output-dir "target/surefire-reports"}}})
