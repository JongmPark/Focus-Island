import streamlit as st
import pandas as pd
import plotly.express as px
import random
import time

# -------------------------------------------------------------
# 1. LATIN & GLOBAL MULTILINGUAL DICTIONARY (현지화 딕셔너리)
# -------------------------------------------------------------
LOCALIZATION_DICT = {
    'ko': {
        'app_title': "포커스 아일랜드 🏝️",
        'app_subtitle': "미니멀리즘 일과 추적기 & 감성 아일랜드 키우기",
        'lang_select': "🌐 언어 선택 / Language",
        'premium_active': "Premium 활성화됨 🌟 (광고 제거 & 비주얼 혜택 적용)",
        'premium_inactive': "무료 체험 버전 (하단에 시뮬레이티드 광고 노출)",
        'premium_btn': "포커스 프리미엄 업그레이드 ($0 - 무료 체험)",
        'premium_downgrade': "프리미엄 끄기 (데모)",
        'today_progress': "📈 오늘의 루틴 달성도",
        'congrats_all': "🎉 축하합니다! 오늘의 모든 집중 과제를 달성해 섬에 무지개가 떴습니다!",
        'congrats_interstitial': "✨ [프리미엄 알림] 전면 마일스톤 달성 축하 다이얼로그 노출! (광고 스킵됨)",
        'not_completed_yet': "아직 완료하지 않은 루틴이 있습니다. 조금만 더 힘내세요!",
        'no_routines': "오늘 등록된 루틴이 없습니다. 아래에서 나만의 미니멀 소박한 루틴을 추가해보세요!",
        'form_title': "🆕 새 루틴 추가하기",
        'input_placeholder': "예: 15분 독서, 아침 일찍 물 한 잔, 가벼운 스트레칭...",
        'color_select': "🎨 라벨 감성 파스텔 색상",
        'add_btn': "루틴 저장하기 🌴",
        'routine_list_header': "🔑 오늘의 실천 체크리스트",
        'delete': "삭제",
        'completed': "완료",
        'chart_title': "일일 루틴 집중 강도 분석",
        'chart_x': "루틴 분류",
        'chart_y': "중요도 가중치 (%)",
        'chart_legend': "분류 상태",
        'ad_banner_title': "⬇️ [시뮬레이션 구글 애드몹 배너 광고] ⬇️",
        'ad_text': "☕ '지친 일상 속, 나만의 따뜻한 카페 소리' - 마음 안정이 필요한 당신을 위한 CalmSound 다운로드!",
    },
    'en': {
        'app_title': "Focus Island 🏝️",
        'app_subtitle': "Minimalist Habit Tracker & Aesthetic Island Growth",
        'lang_select': "🌐 Choose Language / 언어 선택",
        'premium_active': "Premium Active 🌟 (Ads Removed & Ultra Visuals Enhanced)",
        'premium_inactive': "Free Trial Version (Simulated Banner Ads shown below)",
        'premium_btn': "Upgrade to Focus Premium ($0 - Free Trial)",
        'premium_downgrade': "Disable Premium (Demo Mode)",
        'today_progress': "📈 Today's Routine Progress",
        'congrats_all': "🎉 Congratulations! All goals completed. A stellar rainbow is shining above your island!",
        'congrats_interstitial': "✨ [Milestone Alert] Fullscreen interstitial dialog triggered! (Skipped due to Premium)",
        'not_completed_yet': "You have active routines remaining. Keep growing your island step by step!",
        'no_routines': "No routines found for today. Create your very first mindful routine below!",
        'form_title': "🆕 Add New Routine",
        'input_placeholder': "e.g., Read 15 mins, Drink one cup of water, Breathe deeply...",
        'color_select': "🎨 Pastel Tag Color Palette",
        'add_btn': "Save Routine 🌴",
        'routine_list_header': "🔑 Today's Progress Checklist",
        'delete': "Delete",
        'completed': "Done",
        'chart_title': "Daily Focus Density Analytics",
        'chart_x': "Routine Categories",
        'chart_y': "Aesthetic Weight (%)",
        'chart_legend': "Status",
        'ad_banner_title': "⬇️ [Simulated Google AdMob Banner] ⬇️",
        'ad_text': "☕ 'Warm background cafe sounds just for you' - Download CalmSound App for your daily focus session!",
    },
    'es': {
        'app_title': "Focus Island 🏝️",
        'app_subtitle': "Seguidor de Hábitos Minimalista e Crecimiento de Isla",
        'lang_select': "🌐 Seleccionar Idioma",
        'premium_active': "Premium Activo 🌟 (Anuncios Eliminados y Estética Mejorada)",
        'premium_inactive': "Versión de Prueba Gratuita (Anuncio Simulado abajo)",
        'premium_btn': "Mejorar a Focus Premium ($0 - Prueba Gratis)",
        'premium_downgrade': "Desactivar Premium (Demo)",
        'today_progress': "📈 Progreso de Rutinas de Hoy",
        'congrats_all': "🎉 ¡Felicidades! Todo completado. ¡Un arcoíris mágico brilla sobre tu hermosa isla!",
        'congrats_interstitial': "✨ [Alerta de Logro] ¡Mensaje emergente de pantalla completa mostrado! (Omitido por Premium)",
        'not_completed_yet': "¡Quedan rutinas activas! Sigue haciendo crecer tu isla poco a poco.",
        'no_routines': "No hay rutinas programadas hoy. ¡Crea tu primera rutina consciente abajo!",
        'form_title': "🆕 Añadir Nueva Rutina",
        'input_placeholder': "Ej. Leer 15 mins, Beber agua, Respirar profundamente...",
        'color_select': "🎨 Color de Etiqueta Pastel",
        'add_btn': "Guardar Rutina 🌴",
        'routine_list_header': "🔑 Lista de Tareas Diarias",
        'delete': "Eliminar",
        'completed': "Completado",
        'chart_title': "Análisis de Densidad de Enfoque Diario",
        'chart_x': "Categorías de Rutina",
        'chart_y': "Peso Estético (%)",
        'chart_legend': "Estado",
        'ad_banner_title': "⬇️ [Anuncio de Banner AdMob Simulado] ⬇️",
        'ad_text': "☕ 'Sonidos reconfortantes de cafetería' - ¡Descarga CalmSound para relajarte hoy!",
    },
    'pt': {
        'app_title': "Focus Island 🏝️",
        'app_subtitle': "Rastreador de Hábitos Minimalista & Crescimento da Ilha",
        'lang_select': "🌐 Escolher Idioma",
        'premium_active': "Premium Ativo 🌟 (Sem Anúncios e Estética Visual Aumentada)",
        'premium_inactive': "Versão de Teste Grátis (Banner de Anúncio Simulado abaixo)",
        'premium_btn': "Atualizar para Focus Premium ($0 - Teste Grátis)",
        'premium_downgrade': "Desativar Premium (Modo Demo)",
        'today_progress': "📈 Progresso das Rotinas de Hoje",
        'congrats_all': "🎉 Parabéns! Tudo concluído. Um arco-íris maravilhoso está brilhando na sua ilha!",
        'congrats_interstitial': "✨ [Alerta de Conquista] Anúncio em tela cheia simulado acionado! (Ignorado no Premium)",
        'not_completed_yet': "Você ainda tem rotinas pendentes. Caminhe firme para ver sua ilha florescer!",
        'no_routines': "Crie sua primeira rotina diária no painel abaixo agora mesmo!",
        'form_title': "🆕 Adicionar Nova Rotina",
        'input_placeholder': "Ex: Ler por 15 min, Beber água, Meditar...",
        'color_select': "🎨 Paleta de Cores Pastel",
        'add_btn': "Salvar Rotina 🌴",
        'routine_list_header': "🔑 Lista de Hábitos do Dia",
        'delete': "Excluir",
        'completed': "Concluído",
        'chart_title': "Análise de Foco e Densidade Diária",
        'chart_x': "Categorias de Hábitos",
        'chart_y': "Peso do Hábito (%)",
        'chart_legend': "Status",
        'ad_banner_title': "⬇️ [Banner de Anúncio AdMob Simulado] ⬇️",
        'ad_text': "☕ 'Sons aconchegantes de café só para você' - Baixe CalmSound para se concentrar!",
    },
    'id': {
        'app_title': "Focus Island 🏝️",
        'app_subtitle': "Pelacak Rutinitas Minimalis & Pertumbuhan Pulau Visual",
        'lang_select': "🌐 Pilih Bahasa / Language",
        'premium_active': "Premium Aktif 🌟 (Bebas Iklan & Bonus Estetika Pulau)",
        'premium_inactive': "Versi Gratis (Iklan Banner Simulasi di bawah)",
        'premium_btn': "Tingkatkan ke Focus Premium ($0 - Uji Coba Gratis)",
        'premium_downgrade': "Matikan Premium (Demo)",
        'today_progress': "📈 Kemajuan Rutinitas Hari Ini",
        'congrats_all': "🎉 Selamat! Semua tugas selesai. Pelangi indah kini menghiasi langit Pulau Fokusmu!",
        'congrats_interstitial': "✨ [Milestone Alert] Iklan Interstitial Layar Penuh Muncul! (Terlewati berkat Premium)",
        'not_completed_yet': "Selesaikan sisa rutinitas untuk menyaksikan pulaumu tumbuh indah!",
        'no_routines': "Belum ada rutinitas hari ini. Buat kebiasaan sehatmu sekarang di bawah!",
        'form_title': "🆕 Tambah Rutinitas Baru",
        'input_placeholder': "Misal: Membaca 15 mnt, Minum air segar, Latihan nafas...",
        'color_select': "🎨 Pilihan Warna Pastel",
        'add_btn': "Simpan Rutinitas 🌴",
        'routine_list_header': "🔑 Daftar Cek Rutinitas Hari Ini",
        'delete': "Hapus",
        'completed': "Selesai",
        'chart_title': "Analisis Kepadatan Fokus Harian",
        'chart_x': "Kategori Rutinitas",
        'chart_y': "Bobot Fokus (%)",
        'chart_legend': "Status",
        'ad_banner_title': "⬇️ [Iklan AdMob Banner Simulasi] ⬇️",
        'ad_text': "☕ 'Suara ambient kafe yang hangat' - Unduh CalmSound App untuk ketenangan pikiran!",
    },
    'th': {
        'app_title': "Focus Island 🏝️",
        'app_subtitle': "เครื่องมือบันทึกกิจวัตรแบบมินิมอล & เกาะเติบโตแสนผ่อนคลาย",
        'lang_select': "🌐 เลือกภาษา (Choose Language)",
        'premium_active': "พรีเมียมเปิดใช้งานแล้ว 🌟 (ลบโฆษณา & เพิ่มสิทธิพิเศษด้านภาพเกาะ)",
        'premium_inactive': "เวอร์ชันฟรี (มีแถบโฆษณาจำลองด้านล่างสุด)",
        'premium_btn': "อัปเกรดเป็น Focus Premium ($0 - ทดลองใช้ฟรี)",
        'premium_downgrade': "ปิดโหมดพรีเมียม (เดโม)",
        'today_progress': "📈 ความคืบหน้าของกิจวัตรประจำวัน",
        'congrats_all': "🎉 ยินดีด้วย! คุณทำภารกิจครบแล้วร้อยเปอร์เซ็นต์ มีรุ้งกินน้ำแสนสวยงามพาดผ่านเกาะคุณ!",
        'congrats_interstitial': "✨ [ป๊อปอัปความสำเร็จ] โฆษณาคั่นหน้าจอโผล่เตือนความสำเร็จ! (ข้ามโฆษณาเพราะคุณใช้พรีเมียม)",
        'not_completed_yet': "คุณยังเหลือภารกิจที่ยังไม่ได้ทำ สู้ต่อไปเพื่อช่วยให้เกาะเบ่งบานชวนมอง!",
        'no_routines': "วันนี้ยังไม่ได้ลงบันทึกกิจวัตร มาเริ่มต้นทำสิ่งดีๆ ด้วยการบันทึกด้านล่างคลิกเลย!",
        'form_title': "🆕 เพิ่มกิจวัตรประจำวันใหม่",
        'input_placeholder': "เช่น อ่านหนังสือก่อนนอน 15 นาที, ดื่มน้ำแก้วใหญ่, ฝึกสมาธิ...",
        'color_select': "🎨 เลือกสีแท็กพาสเทล",
        'add_btn': "บันทึกกิจวัตร 🌴",
        'routine_list_header': "🔑 รายงานเช็คลิสต์ประจำวัน",
        'delete': "ลบออก",
        'completed': "สำเร็จแล้ว",
        'chart_title': "แผนภูมิจุดความถี่ในการโฟกัสรายวัน",
        'chart_x': "หมวดหมู่รายการ",
        'chart_y': "ระดับคะแนนความตั้งใจ (%)",
        'chart_legend': "สถานะดัชนี",
        'ad_banner_title': "⬇️ [พื้นที่แสดงแถบโฆษณา AdMob จำลอง] ⬇️",
        'ad_text': "☕ 'คลื่นเสียงวิทยุคาเฟ่น่านั่งสำหรับคนรักงานคงเดิม' - โหลด CalmSound เลยวันนี้!",
    },
    'vi': {
        'app_title': "Focus Island 🏝️",
        'app_subtitle': "Theo Dõi Thói Quen Tối Giản & Phát Triển Đảo Cảnh Quan",
        'lang_select': "🌐 Chọn Ngôn Ngữ / Language",
        'premium_active': "Premium Đang Hoạt Động 🌟 (Miễn Phí Quảng Cáo & Hiệu Ứng Siêu Đẹp)",
        'premium_inactive': "Bản Dùng Thử Miền Phí (Hiển thị quảng cáo giả lập phía dưới)",
        'premium_btn': "Nâng cấp lên Focus Premium ($0 - Dùng thử miễn phí)",
        'premium_downgrade': "Tắt Premium (Demo)",
        'today_progress': "📈 Tiến Độ Thói Quen Hôm Nay",
        'congrats_all': "🎉 Xin chúc mừng! Bạn đã hoàn thành tất cả. Cầu vồng rực rỡ tuyệt đẹp xuất hiện trên hòn đảo của bạn!",
        'congrats_interstitial': "✨ [Thông báo] Quảng cáo chuyển tiếp toàn màn hình bật lên chúc mừng! (Được ẩn do bạn dùng Premium)",
        'not_completed_yet': "Vẫn còn một số thói quen chưa làm. Tiếp tục nỗ lực để đảo của bạn tươi tốt hơn!",
        'no_routines': "Chưa có thói quen nào hôm nay. Hãy tạo thói quen sống lành mạnh ở bảng dưới nhé!",
        'form_title': "🆕 Thêm Thói Quen Mới",
        'input_placeholder': "Ví dụ: Đọc sách 15 phút, Uống nước thanh lọc, Thở sâu...",
        'color_select': "🎨 Chọn Bảng Màu Thẻ Pastel",
        'add_btn': "Lưu Thói Quen 🌴",
        'routine_list_header': "🔑 Danh Sách Thói Quen Hôm Nay",
        'delete': "Xóa bỏ",
        'completed': "Hoàn thành",
        'chart_title': "Phân Tích Mức Độ Tập Trung Của Thói Quen Hàng Ngày",
        'chart_x': "Phân Nhóm Thói Quen",
        'chart_y': "Trọng Số Thẩm Mỹ (%)",
        'chart_legend': "Trạng thái",
        'ad_banner_title': "⬇️ [Quảng Cáo Banner AdMob Giả Lập] ⬇️",
        'ad_text': "☕ 'Âm thanh nền quán cà phê dịu êm nhẹ nhàng' - Tải nhanh CalmSound để thư thái đầu óc!",
    }
}

# -------------------------------------------------------------
# 2. STREAMLIT CONFIG & STATE INITIALIZATION
# -------------------------------------------------------------
st.set_page_config(
    page_title="Focus Island 🏝️",
    page_icon="🏝️",
    layout="centered",
    initial_sidebar_state="expanded"
)

# Custom Style Sheet (MUJI Minimal Sand Tone Aesthetics)
st.markdown("""
<style>
    @import url('https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@400;600;700&display=swap');
    
    html, body, [data-testid="stAppViewContainer"] {
        background-color: #F8F6F2 !important;
        font-family: 'Space Grotesk', -apple-system, sans-serif !important;
        color: #4A4A4A !important;
    }
    .main-title {
        font-size: 2.8rem;
        font-weight: 700;
        color: #2F3E33;
        text-align: center;
        margin-bottom: 0.1rem;
    }
    .subtitle {
        text-align: center;
        font-size: 1.1rem;
        color: #7E8B80;
        margin-bottom: 2rem;
    }
    .island-card {
        background-color: #FFFFFF;
        border: 1px solid #EAE5DB;
        border-radius: 20px;
        padding: 24px;
        box-shadow: 0 4px 15px rgba(126, 139, 128, 0.08);
        text-align: center;
        margin-bottom: 1.8rem;
    }
    .ad-banner {
        background-color: #FFFDF9;
        border: 2px dashed #E5DBC7;
        border-radius: 12px;
        padding: 16px;
        text-align: center;
        margin-top: 3rem;
    }
    .ad-title {
        font-size: 0.75rem;
        font-weight: bold;
        color: #BC9B6A;
        letter-spacing: 1px;
        text-transform: uppercase;
        margin-bottom: 6px;
    }
    .ad-body {
        font-size: 0.85rem;
        color: #8C7F6B;
        font-style: italic;
    }
</style>
""", unsafe_allow_html=True)

# Session state initialization for database routines
if 'routines' not in st.session_state:
    st.session_state.routines = [
        {"id": 1, "task": "Acup cup of clean water 💧", "color": "🌿 Sage Green", "is_completed": True},
        {"id": 2, "task": "Read 15 Pages of Book 📖", "color": "🍑 Soft Coral", "is_completed": False},
        {"id": 3, "task": "Stretching for 5 mins 🧘", "color": "🌾 Warm Straw", "is_completed": False}
    ]

if 'premium' not in st.session_state:
    st.session_state.premium = False

# -------------------------------------------------------------
# 3. SIDEBAR CONTROLS (언어 선택 & 프리미엄 스위치)
# -------------------------------------------------------------
st.sidebar.markdown("<h2 style='color:#2F3E33; font-weight:700;'>Focus Island 🏝️</h2>", unsafe_allow_html=True)

# Selectbox Language List with Flags
lang_options = {
    'ko': "🇰🇷 한국어",
    'en': "🇺🇸 English",
    'es': "🇲🇽 Español",
    'pt': "🇧🇷 Português",
    'id': "🇮🇩 Bahasa Indonesia",
    'th': "🇹🇭 ภาษาไทย",
    'vi': "🇻🇳 Tiếng Việt"
}

selected_lang_label = st.sidebar.selectbox(
    "🌐 Choose Language / 언어 선택",
    options=list(lang_options.values()),
    index=0
)

# Get the language code back from selected label
lang_code = [k for k, v in lang_options.items() if v == selected_lang_label][0]
t = LOCALIZATION_DICT[lang_code]

# Premium Toggle on sidebar
st.sidebar.write("---")
st.sidebar.markdown(f"### {'💎 Membership / 멤버십' if lang_code == 'ko' else '💎 Membership'}")

if st.session_state.premium:
    st.sidebar.success(t['premium_active'])
    if st.sidebar.button(t['premium_downgrade']):
        st.session_state.premium = False
        st.rerun()
else:
    st.sidebar.info(t['premium_inactive'])
    if st.sidebar.button(t['premium_btn']):
        st.session_state.premium = True
        st.rerun()

# Information Box on Sidebar
st.sidebar.write("---")
st.sidebar.caption(
    "✨ **Focus Island (Streamlit Version)**\n\n"
    "This web model shows how easy localization and adaptive design work in real-time. "
    "Designed with high-contrast pastel palettes and automatic fluid layout systems."
)

# -------------------------------------------------------------
# 4. SCENIC VISUAL ISLAND GROWTH (Jetpack Canvas SVG 모방 구현)
# -------------------------------------------------------------
# Calculating completion rates
total_count = len(st.session_state.routines)
completed_count = sum(1 for r in st.session_state.routines if r['is_completed'])
completion_percent = int((completed_count / total_count * 100)) if total_count > 0 else 0

# Selecting design colors matching premium settings
sky_gradient_start = "#D9E8F5" if not st.session_state.premium else "#141E30"
sky_gradient_end = "#F8F6F2" if not st.session_state.premium else "#243B55"
island_sand_color = "#EADCC3" if not st.session_state.premium else "#ECC590"
sea_light_color = "#C3E2DF"
sky_star_visibility = "visible" if (completion_percent >= 100 or st.session_state.premium) else "hidden"

# Determining aesthetic decorations based on progress steps
decorations = ""
ambient_sway = """
@keyframes sway {
    0% { transform: rotate(0deg); }
    50% { transform: rotate(3deg); }
    100% { transform: rotate(0deg); }
}
.sway-element {
    transform-origin: 150px 140px;
    animation: sway 4s ease-in-out infinite;
}
"""

# Sprout State
if 1 <= completion_percent < 30:
    # Small single growing leafy bud sprout
    decorations += """
    <path class="sway-element" d="M 150 140 Q 145 125 142 110 Q 148 108 152 115 Q 155 128 150 140" fill="#789A82" />
    <path class="sway-element" d="M 150 130 Q 162 125 168 120 Q 165 115 156 122" fill="#99BAA3" />
    """
# Palm Tree State
elif 30 <= completion_percent < 60:
    # Majestic swaying palm tree trunk and leaves
    decorations += """
    <path class="sway-element" d="M 148 140 L 145 70 Q 155 70 152 140 Z" fill="#7D5C40" />
    <!-- Leaves -->
    <path class="sway-element" d="M 145 70 Q 120 75 100 90 C 120 85 140 80 145 70" fill="#5F8A6B" />
    <path class="sway-element" d="M 145 70 Q 130 50 115 40 C 130 50 140 60 145 70" fill="#4B6E54" />
    <path class="sway-element" d="M 145 70 Q 160 50 175 40 C 160 50 150 60 145 70" fill="#5F8A6B" />
    <path class="sway-element" d="M 145 70 Q 170 75 190 90 C 170 85 150 80 145 70" fill="#4B6E54" />
    """
# Bloomed Flowers state
elif 60 <= completion_percent < 100:
    # Swaying palm tree and charming tropical flowers around
    decorations += """
    <path class="sway-element" d="M 148 140 L 145 70 Q 155 70 152 140 Z" fill="#7D5C40" />
    <path class="sway-element" d="M 145 70 Q 120 75 100 90 C 120 85 140 80 145 70" fill="#5F8A6B" />
    <path class="sway-element" d="M 145 70 Q 130 50 115 40 C 130 50 140 60 145 70" fill="#4B6E54" />
    <path class="sway-element" d="M 145 70 Q 160 50 175 40 C 160 50 150 60 145 70" fill="#5F8A6B" />
    <path class="sway-element" d="M 145 70 Q 170 75 190 90 C 170 85 150 80 145 70" fill="#4B6E54" />
    <!-- Flowers on sandbank -->
    <circle cx="115" cy="142" r="6" fill="#E67E22" />
    <circle cx="111" cy="142" r="4" fill="#F1C40F" /> <circle cx="119" cy="142" r="4" fill="#F1C40F" />
    <circle cx="115" cy="138" r="4" fill="#F1C40F" /> <circle cx="115" cy="146" r="4" fill="#F1C40F" />
    
    <circle cx="185" cy="144" r="5" fill="#E74C3C" />
    <circle cx="181" cy="144" r="3.5" fill="#F39C12" /> <circle cx="189" cy="144" r="3.5" fill="#F39C12" />
    <circle cx="185" cy="140" r="3.5" fill="#F39C12" /> <circle cx="185" cy="148" r="3.5" fill="#F39C12" />
    """
# Rainbow & Twinkling Cosmos (100% Complete)
elif completion_percent >= 100:
    decorations += """
    <!-- Super Majestic Rainbow arching in sky -->
    <path d="M 40 145 A 110 110 0 0 1 260 145" fill="none" stroke="#E74C3C" stroke-width="4" stroke-linecap="round" opacity="0.8" />
    <path d="M 48 145 A 102 102 0 0 1 252 145" fill="none" stroke="#F1C40F" stroke-width="4" stroke-linecap="round" opacity="0.8" />
    <path d="M 56 145 A 94 94 0 0 1 244 145" fill="none" stroke="#9B59B6" stroke-width="4" stroke-linecap="round" opacity="0.8" />
    <path d="M 64 145 A 86 86 0 0 1 236 145" fill="none" stroke="#3498DB" stroke-width="4" stroke-linecap="round" opacity="0.8" />
    <path d="M 72 145 A 78 78 0 0 1 228 145" fill="none" stroke="#2ECC71" stroke-width="4" stroke-linecap="round" opacity="0.8" />

    <!-- Palm Tree in center -->
    <path class="sway-element" d="M 148 140 L 145 70 Q 155 70 152 140 Z" fill="#7D5C40" />
    <path class="sway-element" d="M 145 70 Q 120 75 100 90 C 120 85 140 80 145 70" fill="#5F8A6B" />
    <path class="sway-element" d="M 145 70 Q 130 50 115 40 C 130 50 140 60 145 70" fill="#4B6E54" />
    <path class="sway-element" d="M 145 70 Q 160 50 175 40 C 160 50 150 60 145 70" fill="#5F8A6B" />
    <path class="sway-element" d="M 145 70 Q 170 75 190 90 C 170 85 150 80 145 70" fill="#4B6E54" />
    
    <!-- Twinkling stars glowing in sky -->
    <polygon points="60,30 62,35 68,36 63,40 64,46 60,42 56,46 57,40 52,36 58,35" fill="#FFF" opacity="0.9"/>
    <polygon points="240,40 241,43 245,44 242,47 243,51 240,49 237,51 238,47 235,44 239,43" fill="#FFEAA7" opacity="0.85"/>
    <polygon points="150,20 151,23 155,24 152,27 153,31 150,29 147,31 148,27 145,24 149,23" fill="#FFF" opacity="0.95"/>
    """

# Dynamic SVG Canvas Generator
svg_html = f"""
<div style="display: flex; justify-content: center; align-items: center; width: 100%;">
    <svg viewBox="0 0 300 180" style="max-width: 420px; width: 100%; border-radius: 20px; border: 1.5px solid #E3DEC6; background: linear-gradient(to bottom, {sky_gradient_start}, {sky_gradient_end}); box-shadow: inset 0 2px 8px rgba(0,0,0,0.06);">
        <style>
            {ambient_sway}
            @keyframes twinkle {{
                0% {{ opacity: 0.3; }}
                50% {{ opacity: 1; }}
                100% {{ opacity: 0.3; }}
            }}
            .star-twinkle {{
                animation: twinkle 2s infinite ease-in-out;
            }}
        </style>
        
        <!-- Stars layer (visible always in Premium, or at 100% in Free) -->
        <g style="visibility: {sky_star_visibility}">
            <circle cx="40" cy="35" r="1.5" fill="#FFF" class="star-twinkle" />
            <circle cx="100" cy="20" r="1" fill="#FFF" class="star-twinkle" style="animation-delay: 0.5s;" />
            <circle cx="210" cy="25" r="1.5" fill="#FFF" class="star-twinkle" style="animation-delay: 1.2s;" />
            <circle cx="270" cy="45" r="1.2" fill="#FFA500" class="star-twinkle" style="animation-delay: 0.8s;" />
        </g>
        
        <!-- Sun/Moon depending on premium theme -->
        <circle cx="250" cy="45" r="15" fill="{"#FFF4D4" if not st.session_state.premium else "#FFFDF0"}" opacity="{"0.9" if not st.session_state.premium else "0.15"}" />
        
        <!-- Floating central Sandbank Island structure -->
        <path d="M 60 145 Q 150 120 240 145 Q 260 155 230 160 Q 150 168 70 160 Q 40 155 60 145 Z" fill="{island_sand_color}" />
        <path d="M 70 148 Q 150 132 230 148" stroke="#E3CFA9" stroke-width="2" fill="none" opacity="0.6" />

        <!-- Dynamic Scenic Grow Decorations -->
        {decorations}

        <!-- Ocean backdrop -->
        <path d="M 0 155 Q 150 145 300 155 L 300 180 L 0 180 Z" fill="{sea_light_color}" opacity="0.55" />
        <path d="M 0 160 Q 150 152 300 160" stroke="#AFD0CE" stroke-width="1.5" fill="none" opacity="0.8" />
        
        <!-- Interactive completion percentage metric printed onto island canvas -->
        <text x="150" y="172" fill="#5F7366" font-size="9" font-weight="bold" text-anchor="middle" letter-spacing="1">
            FOCUS ISLAND: {completion_percent}%
        </text>
    </svg>
</div>
"""

# Header Title Rendering
st.markdown(f"<h1 class='main-title'>{t['app_title']}</h1>", unsafe_allow_html=True)
st.markdown(f"<div class='subtitle'>{t['app_subtitle']}</div>", unsafe_allow_html=True)

# -------------------------------------------------------------
# 5. CORE INTERACTIVE CANVAS CONTAINER
# -------------------------------------------------------------
with st.container():
    st.markdown("<div class='island-card'>", unsafe_allow_html=True)
    st.write(f"### {t['today_progress']} ({completed_count}/{total_count})")
    
    # Progress Bar UI Indicator
    st.progress(completed_count / total_count if total_count > 0 else 0)
    
    # Injecting Island SVG Canvas
    st.markdown(svg_html, unsafe_allow_html=True)
    
    # Text notification updates below island canvas
    if total_count == 0:
        st.write(t['no_routines'])
    elif completed_count == total_count:
        st.success(t['congrats_all'])
        if not st.session_state.premium:
            st.warning("⚠️ " + "Congratulations Ad interstitial shown below! Upgrade to Premium to completely bypass.")
        else:
            st.info(t['congrats_interstitial'])
    else:
        st.info(t['not_completed_yet'])
    st.markdown("</div>", unsafe_allow_html=True)

# -------------------------------------------------------------
# 6. ROUTINE MANAGEMENT (체크리스트 추가, 토글, 삭제)
# -------------------------------------------------------------
col1, col2 = st.columns([1, 1])

with col1:
    st.markdown(f"### {t['routine_list_header']}")
    
    # Render interactive routines list
    for index, r in enumerate(st.session_state.routines):
        routine_key = f"chk_{r['id']}"
        
        # Color bullet matching their selected pastel label
        bullet_icon = "🟢"
        if r['color'] == "🍑 Soft Coral":
            bullet_icon = "🍑"
        elif r['color'] == "🌾 Warm Straw":
            bullet_icon = "🌾"
        elif r['color'] == "💡 Clear Slate":
            bullet_icon = "💡"
            
        r_cols = st.columns([0.1, 0.7, 0.2])
        
        # Checked status toggle
        is_done = r_cols[0].checkbox("", value=r['is_completed'], key=f"chk_cb_{r['id']}")
        
        # Updating the routine completed state reactively
        if is_done != r['is_completed']:
            st.session_state.routines[index]['is_completed'] = is_done
            st.rerun()
            
        # Task title display with custom color tags
        text_style = "text-decoration: line-through; color: #9A9E9A;" if r['is_completed'] else "font-weight: 500; color: #2F3E33;"
        r_cols[1].markdown(f"<span style='{text_style}'>{bullet_icon} {r['task']}</span>", unsafe_allow_html=True)
        
        # Delete Routine button
        if r_cols[2].button("❌", key=f"del_{r['id']}", help=t['delete']):
            st.session_state.routines.pop(index)
            st.rerun()

with col2:
    st.markdown(f"### {t['form_title']}")
    
    with st.form("new_routine_form", clear_on_submit=True):
        input_task = st.text_input(
            "Task / 루틴 이름", 
            placeholder=t['input_placeholder']
        )
        
        color_choices = {
            "🌿 Sage Green": "🌿 Sage Green",
            "🍑 Soft Coral": "🍑 Soft Coral",
            "🌾 Warm Straw": "🌾 Warm Straw",
            "💡 Clear Slate": "💡 Clear Slate"
        }
        
        input_color = st.selectbox(
            t['color_select'],
            options=list(color_choices.keys())
        )
        
        submitted = st.form_submit_with_button_on_click(t['add_btn'])
        
        if submitted and input_task.strip() != "":
            # Unique incremental ID allocation
            new_id = max([r['id'] for r in st.session_state.routines]) + 1 if len(st.session_state.routines) > 0 else 1
            st.session_state.routines.append({
                "id": new_id,
                "task": input_task.strip(),
                "color": input_color,
                "is_completed": False
            })
            st.rerun()

# -------------------------------------------------------------
# 7. LOCALIZED DATA VISUALIZATION CHART (Plotly 연동)
# -------------------------------------------------------------
st.write("---")
st.markdown(f"### 📊 Focus Island Analytics ({t['chart_title']})")

if len(st.session_state.routines) > 0:
    # Build complete localized Dataframe dynamically
    chart_data = []
    for r in st.session_state.routines:
        status_label = t['completed'] if r['is_completed'] else "Active"
        chart_data.append({
            t['chart_x']: r['task'],
            t['chart_y']: 30 if r['is_completed'] else 100,  # Assigning visual metrics based on weight status
            t['chart_legend']: status_label
        })
        
    df = pd.DataFrame(chart_data)
    
    # Custom colored Plotly Bar Chart matching MUJI tone theme
    fig = px.bar(
        df,
        x=t['chart_x'],
        y=t['chart_y'],
        color=t['chart_legend'],
        color_discrete_map={t['completed']: "#789A82", "Active": "#E5DBC7"},
        title=None
    )
    
    # Updating dynamic high-contrast layouts
    fig.update_layout(
        plot_bgcolor="rgba(248, 246, 242, 1)",
        paper_bgcolor="rgba(0,0,0,0)",
        margin=dict(l=20, r=20, t=10, b=20),
        xaxis_title=t['chart_x'],
        yaxis_title=t['chart_y'],
        font=dict(family="Space Grotesk, sans-serif", size=12, color="#4A4A4A"),
    )
    
    st.plotly_chart(fig, use_container_width=True)
else:
    st.caption("No analytics data available. Complete tasks to chart growth densities.")

# -------------------------------------------------------------
# 8. INTERACTIVE AD REVENUE MOCK SANDBOX
# -------------------------------------------------------------
if not st.session_state.premium:
    st.markdown(f"""
    <div class='ad-banner'>
        <div class='ad-title'>{t['ad_banner_title']}</div>
        <div class='ad-body'>{t['ad_text']}</div>
        <div style='margin-top: 8px; font-size: 0.7rem; color: #BBB; font-weight: bold;'>
            ✦ Simulated Google AdMob Integration ID: ca-app-pub-3940256099942544/6300978111
        </div>
    </div>
    """, unsafe_allow_html=True)
else:
    st.write("")
