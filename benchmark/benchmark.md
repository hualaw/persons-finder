
# run a medium load test (50 virtual users for 30s)
BASE_URL=http://localhost:8080 k6 run --vus 50 --duration 30s benchmark/nearby_benchmark.js

# result


         /\      Grafana   /‾‾/
    /\  /  \     |\  __   /  /
   /  \/    \    | |/ /  /   ‾‾\
  /          \   |   (  |  (‾)  |
 / __________ \  |_|\_\  \_____/


     execution: local
        script: benchmark/nearby_benchmark.js
        output: -

     scenarios: (100.00%) 1 scenario, 50 max VUs, 1m0s max duration (incl. graceful stop):
              * default: 50 looping VUs for 30s (gracefulStop: 30s)



  █ TOTAL RESULTS

    checks_total.......: 7380    244.475022/s
    checks_succeeded...: 100.00% 7380 out of 7380
    checks_failed......: 0.00%   0 out of 7380

    ✓ status is 200

    HTTP
    http_req_duration..............: avg=103.5ms  min=30.26ms  med=90.69ms  max=1.27s p(90)=126.31ms p(95)=154.19ms
      { expected_response:true }...: avg=103.5ms  min=30.26ms  med=90.69ms  max=1.27s p(90)=126.31ms p(95)=154.19ms
    http_req_failed................: 0.00%  0 out of 7380
    http_reqs......................: 7380   244.475022/s

    EXECUTION
    iteration_duration.............: avg=203.92ms min=131.31ms med=191.15ms max=1.38s p(90)=226.5ms  p(95)=254.53ms
    iterations.....................: 7380   244.475022/s
    vus............................: 50     min=50        max=50
    vus_max........................: 50     min=50        max=50

    NETWORK
    data_received..................: 2.0 GB 66 MB/s
    data_sent......................: 1.2 MB 39 kB/s

# heavier test
BASE_URL=http://localhost:8080 k6 run --vus 200 --duration 60s benchmark/nearby_benchmark.js



         /\      Grafana   /‾‾/  
    /\  /  \     |\  __   /  /   
   /  \/    \    | |/ /  /   ‾‾\ 
  /          \   |   (  |  (‾)  |
 / __________ \  |_|\_\  \_____/ 


     execution: local
        script: benchmark/nearby_benchmark.js
        output: -

     scenarios: (100.00%) 1 scenario, 200 max VUs, 1m30s max duration (incl. graceful stop):
              * default: 200 looping VUs for 1m0s (gracefulStop: 30s)


running (0m01.0s), 200/200 VUs, 210 complete and 0 interrupted iterations
default   [   2% ] 200 VUs  0m01.0s/1m0s

running (0m02.0s), 200/200 VUs, 470 complete and 0 interrupted iterations
default   [   3% ] 200 VUs  0m02.0s/1m0s

running (0m03.0s), 200/200 VUs, 740 complete and 0 interrupted iterations
default   [   5% ] 200 VUs  0m03.0s/1m0s

running (0m04.0s), 200/200 VUs, 1010 complete and 0 interrupted iterations
default   [   7% ] 200 VUs  0m04.0s/1m0s

running (0m05.0s), 200/200 VUs, 1270 complete and 0 interrupted iterations
default   [   8% ] 200 VUs  0m05.0s/1m0s

running (0m06.0s), 200/200 VUs, 1547 complete and 0 interrupted iterations
default   [  10% ] 200 VUs  0m06.0s/1m0s

running (0m07.0s), 200/200 VUs, 1820 complete and 0 interrupted iterations
default   [  12% ] 200 VUs  0m07.0s/1m0s

running (0m08.0s), 200/200 VUs, 2050 complete and 0 interrupted iterations
default   [  13% ] 200 VUs  0m08.0s/1m0s

running (0m09.0s), 200/200 VUs, 2311 complete and 0 interrupted iterations
default   [  15% ] 200 VUs  0m09.0s/1m0s

running (0m10.0s), 200/200 VUs, 2581 complete and 0 interrupted iterations
default   [  17% ] 200 VUs  0m10.0s/1m0s

running (0m11.0s), 200/200 VUs, 2861 complete and 0 interrupted iterations
default   [  18% ] 200 VUs  0m11.0s/1m0s

running (0m12.0s), 200/200 VUs, 3131 complete and 0 interrupted iterations
default   [  20% ] 200 VUs  0m12.0s/1m0s

running (0m13.0s), 200/200 VUs, 3401 complete and 0 interrupted iterations
default   [  22% ] 200 VUs  0m13.0s/1m0s

running (0m14.0s), 200/200 VUs, 3679 complete and 0 interrupted iterations
default   [  23% ] 200 VUs  0m14.0s/1m0s

running (0m15.0s), 200/200 VUs, 3961 complete and 0 interrupted iterations
default   [  25% ] 200 VUs  0m15.0s/1m0s

running (0m16.0s), 200/200 VUs, 4246 complete and 0 interrupted iterations
default   [  27% ] 200 VUs  0m16.0s/1m0s

running (0m17.0s), 200/200 VUs, 4521 complete and 0 interrupted iterations
default   [  28% ] 200 VUs  0m17.0s/1m0s

running (0m18.0s), 200/200 VUs, 4801 complete and 0 interrupted iterations
default   [  30% ] 200 VUs  0m18.0s/1m0s

running (0m19.0s), 200/200 VUs, 5058 complete and 0 interrupted iterations
default   [  32% ] 200 VUs  0m19.0s/1m0s

running (0m20.0s), 200/200 VUs, 5331 complete and 0 interrupted iterations
default   [  33% ] 200 VUs  0m20.0s/1m0s

running (0m21.0s), 200/200 VUs, 5581 complete and 0 interrupted iterations
default   [  35% ] 200 VUs  0m21.0s/1m0s

running (0m22.0s), 200/200 VUs, 5871 complete and 0 interrupted iterations
default   [  37% ] 200 VUs  0m22.0s/1m0s

running (0m23.0s), 200/200 VUs, 6121 complete and 0 interrupted iterations
default   [  38% ] 200 VUs  0m23.0s/1m0s

running (0m24.0s), 200/200 VUs, 6381 complete and 0 interrupted iterations
default   [  40% ] 200 VUs  0m24.0s/1m0s

running (0m25.0s), 200/200 VUs, 6661 complete and 0 interrupted iterations
default   [  42% ] 200 VUs  0m25.0s/1m0s

running (0m26.0s), 200/200 VUs, 6932 complete and 0 interrupted iterations
default   [  43% ] 200 VUs  0m26.0s/1m0s

running (0m27.0s), 200/200 VUs, 7212 complete and 0 interrupted iterations
default   [  45% ] 200 VUs  0m27.0s/1m0s

running (0m28.0s), 200/200 VUs, 7502 complete and 0 interrupted iterations
default   [  47% ] 200 VUs  0m28.0s/1m0s

running (0m29.0s), 200/200 VUs, 7782 complete and 0 interrupted iterations
default   [  48% ] 200 VUs  0m29.0s/1m0s

running (0m30.0s), 200/200 VUs, 8072 complete and 0 interrupted iterations
default   [  50% ] 200 VUs  0m30.0s/1m0s

running (0m31.0s), 200/200 VUs, 8352 complete and 0 interrupted iterations
default   [  52% ] 200 VUs  0m31.0s/1m0s

running (0m32.0s), 200/200 VUs, 8642 complete and 0 interrupted iterations
default   [  53% ] 200 VUs  0m32.0s/1m0s

running (0m33.0s), 200/200 VUs, 8922 complete and 0 interrupted iterations
default   [  55% ] 200 VUs  0m33.0s/1m0s

running (0m34.0s), 200/200 VUs, 9182 complete and 0 interrupted iterations
default   [  57% ] 200 VUs  0m34.0s/1m0s

running (0m35.0s), 200/200 VUs, 9462 complete and 0 interrupted iterations
default   [  58% ] 200 VUs  0m35.0s/1m0s

running (0m36.0s), 200/200 VUs, 9752 complete and 0 interrupted iterations
default   [  60% ] 200 VUs  0m36.0s/1m0s

running (0m37.0s), 200/200 VUs, 10023 complete and 0 interrupted iterations
default   [  62% ] 200 VUs  0m37.0s/1m0s

running (0m38.0s), 200/200 VUs, 10312 complete and 0 interrupted iterations
default   [  63% ] 200 VUs  0m38.0s/1m0s

running (0m39.0s), 200/200 VUs, 10582 complete and 0 interrupted iterations
default   [  65% ] 200 VUs  0m39.0s/1m0s

running (0m40.0s), 200/200 VUs, 10872 complete and 0 interrupted iterations
default   [  67% ] 200 VUs  0m40.0s/1m0s

running (0m41.0s), 200/200 VUs, 11145 complete and 0 interrupted iterations
default   [  68% ] 200 VUs  0m41.0s/1m0s

running (0m42.0s), 200/200 VUs, 11425 complete and 0 interrupted iterations
default   [  70% ] 200 VUs  0m42.0s/1m0s

running (0m43.0s), 200/200 VUs, 11702 complete and 0 interrupted iterations
default   [  72% ] 200 VUs  0m43.0s/1m0s

running (0m44.0s), 200/200 VUs, 12001 complete and 0 interrupted iterations
default   [  73% ] 200 VUs  0m44.0s/1m0s

running (0m45.0s), 200/200 VUs, 12252 complete and 0 interrupted iterations
default   [  75% ] 200 VUs  0m45.0s/1m0s

running (0m46.0s), 200/200 VUs, 12504 complete and 0 interrupted iterations
default   [  77% ] 200 VUs  0m46.0s/1m0s

running (0m47.0s), 200/200 VUs, 12792 complete and 0 interrupted iterations
default   [  78% ] 200 VUs  0m47.0s/1m0s

running (0m48.0s), 200/200 VUs, 13072 complete and 0 interrupted iterations
default   [  80% ] 200 VUs  0m48.0s/1m0s

running (0m49.0s), 200/200 VUs, 13362 complete and 0 interrupted iterations
default   [  82% ] 200 VUs  0m49.0s/1m0s

running (0m50.0s), 200/200 VUs, 13642 complete and 0 interrupted iterations
default   [  83% ] 200 VUs  0m50.0s/1m0s

running (0m51.0s), 200/200 VUs, 13906 complete and 0 interrupted iterations
default   [  85% ] 200 VUs  0m51.0s/1m0s

running (0m52.0s), 200/200 VUs, 14192 complete and 0 interrupted iterations
default   [  87% ] 200 VUs  0m52.0s/1m0s

running (0m53.0s), 200/200 VUs, 14472 complete and 0 interrupted iterations
default   [  88% ] 200 VUs  0m53.0s/1m0s

running (0m54.0s), 200/200 VUs, 14742 complete and 0 interrupted iterations
default   [  90% ] 200 VUs  0m54.0s/1m0s

running (0m55.0s), 200/200 VUs, 15012 complete and 0 interrupted iterations
default   [  92% ] 200 VUs  0m55.0s/1m0s

running (0m56.0s), 200/200 VUs, 15292 complete and 0 interrupted iterations
default   [  93% ] 200 VUs  0m56.0s/1m0s

running (0m57.0s), 200/200 VUs, 15570 complete and 0 interrupted iterations
default   [  95% ] 200 VUs  0m57.0s/1m0s

running (0m58.0s), 200/200 VUs, 15861 complete and 0 interrupted iterations
default   [  97% ] 200 VUs  0m58.0s/1m0s

running (0m59.0s), 200/200 VUs, 16142 complete and 0 interrupted iterations
default   [  98% ] 200 VUs  0m59.0s/1m0s

running (1m00.0s), 200/200 VUs, 16422 complete and 0 interrupted iterations
default   [ 100% ] 200 VUs  1m00.0s/1m0s


  █ TOTAL RESULTS 

    checks_total.......: 16631   273.793708/s
    checks_succeeded...: 100.00% 16631 out of 16631
    checks_failed......: 0.00%   0 out of 16631

    ✓ status is 200

    HTTP
    http_req_duration..............: avg=625.38ms min=27.29ms  med=618.53ms max=1.92s p(90)=674.78ms p(95)=712.33ms
      { expected_response:true }...: avg=625.38ms min=27.29ms  med=618.53ms max=1.92s p(90)=674.78ms p(95)=712.33ms
    http_req_failed................: 0.00%  0 out of 16631
    http_reqs......................: 16631  273.793708/s

    EXECUTION
    iteration_duration.............: avg=725.96ms min=127.38ms med=718.94ms max=2.03s p(90)=775.34ms p(95)=815.1ms 
    iterations.....................: 16631  273.793708/s
    vus............................: 200    min=200        max=200
    vus_max........................: 200    min=200        max=200

    NETWORK
    data_received..................: 4.5 GB 74 MB/s
    data_sent......................: 2.6 MB 44 kB/s




running (1m00.7s), 000/200 VUs, 16631 complete and 0 interrupted iterations
default ✓ [ 100% ] 200 VUs  1m0s
