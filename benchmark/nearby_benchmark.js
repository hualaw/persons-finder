// k6 benchmark script for /api/v1/persons/nearby
// Usage: k6 run --vus 50 --duration 30s nearby_benchmark.js

import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  vus: 50,
  duration: '30s',
};

const BASE = __ENV.BASE_URL || 'http://localhost:8080';

export default function () {
  // Random point near NYC (adjust based on populate_1m bounding box)
  const lat = 40.7 + (Math.random() - 0.5) * 0.2; // ~40.6..40.8
  const lon = -74.0 + (Math.random() - 0.5) * 0.2; // ~-74.1..-73.9
  const radius = 1000; // meters

  const res = http.get(`${BASE}/api/v1/persons/nearby?latitude=${lat}&longitude=${lon}&radius=${radius}`);
  check(res, {
    'status is 200': (r) => r.status === 200,
  });
  sleep(0.1);
}

//import http from 'k6/http';
//import { check, sleep } from 'k6';
//
//export let options = {
//  vus: 10,
//  duration: '10s',
//};
//
//const BASE = (__ENV.BASE_URL || 'http://localhost:8080').trim();
//
//export default function () {
//  const lat = 40.7 + (Math.random() - 0.5) * 0.2;
//  const lon = -74.0 + (Math.random() - 0.5) * 0.2;
//  const radius = 1000;
//
//  // encode params to avoid any illegal characters
//  const qlat = encodeURIComponent(lat);
//  const qlon = encodeURIComponent(lon);
//  const qradius = encodeURIComponent(radius);
//
//  const url = `${BASE}/api/v1/persons/nearby?latitude=${qlat}&longitude=${qlon}&radius=${qradius}`;
//  // Defensive: strip any backslashes that could have been introduced by escaping
//  const cleanUrl = url.replace(/\\\\/g, '');
//
//
//  // optional: dump char codes when debugging to detect stray/backslash characters
//  if (__ENV.DEBUG_URL === '1') {
//    const codes = [...cleanUrl].map(c => c.charCodeAt(0)).join(',');
//    console.log(`REQUEST_URL: ${cleanUrl}`);
//    console.log(`REQUEST_URL_CHAR_CODES: ${codes}`);
//  } else {
//    console.log(`REQUEST_URL: ${cleanUrl}`);
//  }
//
//  // Defensive: strip any backslashes that could have been introduced by escaping
//  //const cleanUrl = url.replace(/\\/g, '');
//  if (cleanUrl !== url) {
//    console.log('NOTICE: backslashes removed from URL before request');
//    console.log(`CLEAN_REQUEST_URL: ${cleanUrl}`);
//  }
//
//  const res = http.get(cleanUrl);
//
//  // your check
//  const ok = check(res, { 'status is 200': (r) => r.status === 200 });
//
//  // if failed, log status and a short body snippet for debugging
//  if (!ok) {
//    // limit body length to avoid huge logs
//    let bodySnippet = '';
//    try {
//      bodySnippet = res.body ? res.body.toString().slice(0, 300) : '<no body>';
//    } catch (e) {
//      bodySnippet = '<could not read body>';
//    }
//    console.log(`DEBUG: Request failed status=${res.status} body=${bodySnippet}`);
//  }
//
//  sleep(0.1);
//}
