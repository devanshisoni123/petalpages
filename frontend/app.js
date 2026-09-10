/* ═══════════════════════════════════════
   PetalPages — Shared JavaScript
   app.js — common functions used across all pages
═══════════════════════════════════════ */

// ── STORAGE KEYS ──
const PP_KEYS = {
  entries:  'petalpages_guest_entries',
  notes:    'pp_notes',
  period:   'pp_period_tracker',
  settings: 'pp_settings',
  theme:    'pp_journal_theme',
  banner:   'pp_banner_dismissed',
  appPw:    'pp_app_password',
};

// ── GET / SAVE ENTRIES ──
function ppGetEntries() {
  try { return JSON.parse(localStorage.getItem(PP_KEYS.entries)) || []; }
  catch(e) { return []; }
}
function ppSaveEntries(entries) {
  try { localStorage.setItem(PP_KEYS.entries, JSON.stringify(entries)); }
  catch(e) { ppStorageWarning(); }
}

// ── GET / SAVE NOTES ──
function ppGetNotes() {
  try { return JSON.parse(localStorage.getItem(PP_KEYS.notes)) || []; }
  catch(e) { return []; }
}
function ppSaveNotes(notes) {
  try { localStorage.setItem(PP_KEYS.notes, JSON.stringify(notes)); }
  catch(e) { ppStorageWarning(); }
}

// ── GET / SAVE SETTINGS ──
function ppGetSettings() {
  try { return JSON.parse(localStorage.getItem(PP_KEYS.settings)) || {}; }
  catch(e) { return {}; }
}
function ppSaveSettings(s) {
  localStorage.setItem(PP_KEYS.settings, JSON.stringify(s));
}

// ── GET / SAVE PERIOD DATA ──
function ppGetPeriodData() {
  try { return JSON.parse(localStorage.getItem(PP_KEYS.period)) || {}; }
  catch(e) { return {}; }
}
function ppSavePeriodData(d) {
  try { localStorage.setItem(PP_KEYS.period, JSON.stringify(d)); }
  catch(e) { ppStorageWarning(); }
}

// ── TOAST NOTIFICATION ──
function ppToast(msg, duration) {
  duration = duration || 2200;
  let toast = document.getElementById('ppToast');
  if (!toast) {
    toast = document.createElement('div');
    toast.id = 'ppToast';
    toast.className = 'pp-toast';
    document.body.appendChild(toast);
  }
  toast.textContent = msg;
  toast.classList.add('show');
  setTimeout(function() { toast.classList.remove('show'); }, duration);
}

// ── STORAGE USAGE CHECK ──
function ppCheckStorage() {
  try {
    var total = 0;
    for (var key in localStorage) {
      if (localStorage.hasOwnProperty(key)) {
        total += localStorage[key].length * 2; // bytes (UTF-16)
      }
    }
    var usedMB = (total / (1024 * 1024)).toFixed(2);
    var limitMB = 5;
    var pct = Math.round((total / (limitMB * 1024 * 1024)) * 100);
    return { usedMB: parseFloat(usedMB), limitMB: limitMB, pct: pct };
  } catch(e) {
    return { usedMB: 0, limitMB: 5, pct: 0 };
  }
}

function ppStorageWarning() {
  ppToast('⚠️ storage almost full — export your data in settings!', 4000);
}

// ── SAVE STATUS ──
function ppMarkSaving(dotId, textId) {
  dotId = dotId || 'saveDot'; textId = textId || 'saveText';
  var dot = document.getElementById(dotId);
  var txt = document.getElementById(textId);
  if (dot) dot.className = 'pp-save-dot saving';
  if (txt) txt.textContent = 'saving...';
}
function ppMarkSaved(dotId, textId) {
  dotId = dotId || 'saveDot'; textId = textId || 'saveText';
  var dot = document.getElementById(dotId);
  var txt = document.getElementById(textId);
  if (dot) dot.className = 'pp-save-dot saved';
  if (txt) txt.textContent = 'saved';
}
function ppMarkUnsaved(dotId, textId) {
  dotId = dotId || 'saveDot'; textId = textId || 'saveText';
  var dot = document.getElementById(dotId);
  var txt = document.getElementById(textId);
  if (dot) dot.className = 'pp-save-dot';
  if (txt) txt.textContent = 'unsaved';
}

// ── AUTO RESIZE TEXTAREA ──
function ppAutoResize(el) {
  el.style.height = 'auto';
  el.style.height = el.scrollHeight + 'px';
}

// ── WORD COUNT ──
function ppWordCount(text) {
  return text.trim() ? text.trim().split(/\s+/).length : 0;
}

// ── FORMAT DATE ──
function ppFormatDate(dateStr) {
  var d = new Date(dateStr);
  return d.toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' });
}
function ppFormatTime(dateStr) {
  var d = new Date(dateStr);
  return d.toLocaleTimeString('en-IN', { hour: '2-digit', minute: '2-digit' });
}
function ppFormatDateTime(dateStr) {
  return ppFormatDate(dateStr) + ' at ' + ppFormatTime(dateStr);
}

// ── PETAL CONFETTI ──
function ppPetals() {
  var colors = ['#A8C5A0','#C4717A','#D4B060','#E8A0A8','#6B8F6B','#D4E8CC'];
  var style = document.createElement('style');
  style.textContent = '@keyframes ppPetalFall{0%{transform:translateY(-20px) rotate(0deg);opacity:0.75;}100%{transform:translateY(100vh) rotate(500deg) translateX(40px);opacity:0;}}';
  document.head.appendChild(style);
  for (var i = 0; i < 20; i++) {
    var p = document.createElement('div');
    var size = 8 + Math.random() * 10;
    p.style.cssText = 'position:fixed;left:' + (10 + Math.random()*80) + '%;top:-20px;width:' + size + 'px;height:' + (size*1.4) + 'px;background:' + colors[Math.floor(Math.random()*colors.length)] + ';border-radius:60% 40% 60% 40%/50% 60% 40% 50%;opacity:0.75;pointer-events:none;z-index:9999;animation:ppPetalFall ' + (1.5+Math.random()*2) + 's ease-in ' + (Math.random()*0.6) + 's both;';
    document.body.appendChild(p);
    setTimeout(function(el) { return function() { el.remove(); }; }(p), 4000);
  }
}

// ── GUEST BANNER ──
function ppInitGuestBanner(bannerId) {
  bannerId = bannerId || 'guestBar';
  var dismissed = localStorage.getItem(PP_KEYS.banner);
  var banner = document.getElementById(bannerId);
  if (!banner) return;
  if (dismissed === '1') { banner.style.display = 'none'; return; }
  // Add close handler
  var closeBtn = banner.querySelector('.pp-guest-close');
  if (closeBtn) {
    closeBtn.addEventListener('click', function() {
      banner.style.display = 'none';
      localStorage.setItem(PP_KEYS.banner, '1');
    });
  }
}

// ── APP PASSWORD CHECK ──
function ppCheckAppPassword() {
  var s = ppGetSettings();
  if (!s.appPw) return true; // no password set — allow
  var stored = s.appPw;
  var entered = sessionStorage.getItem('pp_session_auth');
  if (entered === stored) return true; // already authed this session
  var input = prompt('Enter your PetalPages password:');
  if (input === stored) {
    sessionStorage.setItem('pp_session_auth', stored);
    return true;
  }
  alert('Incorrect password.');
  window.location.href = 'index.html';
  return false;
}

// ── EXPORT ENTRIES AS JSON ──
function ppExportData() {
  var data = {
    entries: ppGetEntries(),
    notes: ppGetNotes(),
    exportedAt: new Date().toISOString(),
    version: '1.0'
  };
  var blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' });
  var url = URL.createObjectURL(blob);
  var a = document.createElement('a');
  a.href = url;
  a.download = 'petalpages-backup-' + new Date().toISOString().split('T')[0] + '.json';
  a.click();
  URL.revokeObjectURL(url);
  ppToast('✦ exported successfully!');
}

// ── IMPORT DATA ──
function ppImportData(jsonStr) {
  try {
    var data = JSON.parse(jsonStr);
    if (data.entries) ppSaveEntries(data.entries);
    if (data.notes) ppSaveNotes(data.notes);
    ppToast('✦ data imported!');
    return true;
  } catch(e) {
    ppToast('⚠️ invalid file — could not import');
    return false;
  }
}

// ── SETTINGS: APPLY SAVED THEME ──
function ppApplySettings() {
  var s = ppGetSettings();
  if (s.dark) {
    document.body.style.background = '#1A1A2E';
    document.body.style.color = '#E8E0F0';
  }
  if (s.accent) {
    document.documentElement.style.setProperty('--sage', s.accent);
  }
  if (s.accentPale) {
    document.documentElement.style.setProperty('--sage-pale', s.accentPale);
  }
}

// ── SPIRAL CANVAS BACKGROUND ── (used on multiple pages)
function ppDrawSpiralCanvas(canvasId) {
  var canvas = document.getElementById(canvasId);
  if (!canvas) return;
  var ctx = canvas.getContext('2d');
  canvas.width = window.innerWidth;
  canvas.height = window.innerHeight;
  window.addEventListener('resize', function() {
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;
    draw();
  });
  function draw() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    var cx = canvas.width / 2, cy = canvas.height / 2;
    var colors = [
      'rgba(168,197,160,0.32)', 'rgba(196,113,122,0.22)',
      'rgba(212,176,96,0.22)',  'rgba(255,255,255,0.38)'
    ];
    for (var ring = 1; ring <= 10; ring++) {
      var r = ring * 68;
      var dots = Math.floor(ring * 9);
      for (var d = 0; d < dots; d++) {
        var angle = (d / dots) * Math.PI * 2;
        ctx.beginPath();
        ctx.arc(
          cx + Math.cos(angle) * r + (Math.random() * 8 - 4),
          cy + Math.sin(angle) * r + (Math.random() * 8 - 4),
          Math.random() * 2.2 + 0.5, 0, Math.PI * 2
        );
        ctx.fillStyle = colors[Math.floor(Math.random() * colors.length)];
        ctx.fill();
      }
    }
  }
  draw();
}

// ── STORAGE SIZE DISPLAY ──
function ppStorageDisplay() {
  var info = ppCheckStorage();
  return info.usedMB + 'MB used of ' + info.limitMB + 'MB (' + info.pct + '%)';
}

// ── INIT — call on every page load ──
document.addEventListener('DOMContentLoaded', function() {
  ppApplySettings();
  ppInitGuestBanner('guestBar');
  // Warn if storage > 80%
  var info = ppCheckStorage();
  if (info.pct > 80) ppStorageWarning();
});