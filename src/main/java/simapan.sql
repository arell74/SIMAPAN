-- =====================================================
-- SIMAPAN - skema database MySQL (XAMPP / phpMyAdmin)
-- Aman dijalankan berulang: tidak menghapus data yang sudah ada.
-- =====================================================
CREATE DATABASE IF NOT EXISTS simapan
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE simapan;

-- Pewarisan Pengguna -> AdminLPK / Instruktur / Peserta:
-- data umum (username, password, nama_lengkap, role) di `pengguna`,
-- data khusus di `instruktur` dan `peserta` (relasi 1:1 lewat username)
CREATE TABLE IF NOT EXISTS pengguna (
  username     VARCHAR(50)  NOT NULL PRIMARY KEY,
  password     VARCHAR(100) NOT NULL,
  nama_lengkap VARCHAR(100) NOT NULL,
  role         ENUM('ADMIN','INSTRUKTUR','PESERTA') NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS instruktur (
  id_instruktur VARCHAR(10) NOT NULL PRIMARY KEY,
  username      VARCHAR(50) NOT NULL UNIQUE,
  no_telp       VARCHAR(20),
  spesialisasi  VARCHAR(50),
  level_jlpt    VARCHAR(5),
  kelas_diampu  VARCHAR(50),
  CONSTRAINT fk_instruktur_pengguna FOREIGN KEY (username)
    REFERENCES pengguna(username) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS program (
  id_program   VARCHAR(10)  NOT NULL PRIMARY KEY,
  nama_program VARCHAR(100) NOT NULL,
  bidang_usaha VARCHAR(100),
  biaya        DECIMAL(12,2) NOT NULL DEFAULT 0,
  kuota        INT NOT NULL DEFAULT 0
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS peserta (
  id_peserta           VARCHAR(10)  NOT NULL PRIMARY KEY,
  username             VARCHAR(50)  NOT NULL UNIQUE,   -- nama_lengkap & password ada di tabel pengguna
  nik                  VARCHAR(16)  NOT NULL UNIQUE,
  tanggal_lahir        DATE,
  jenis_kelamin        VARCHAR(15),
  no_hp                VARCHAR(20),
  agama                VARCHAR(20),
  alamat               VARCHAR(255),
  id_program           VARCHAR(10)  NOT NULL,
  id_instruktur        VARCHAR(10)  NOT NULL,
  level_bahasa         VARCHAR(5),
  status_pembayaran    VARCHAR(20)  NOT NULL DEFAULT 'Belum Lunas',
  kelas                VARCHAR(50)  NOT NULL DEFAULT 'Belum Ditentukan',
  status_seleksi       VARCHAR(30)  NOT NULL DEFAULT 'Belum Mulai',
  status_keberangkatan VARCHAR(30)  NOT NULL DEFAULT 'Belum Berangkat',
  CONSTRAINT fk_peserta_pengguna FOREIGN KEY (username)
    REFERENCES pengguna(username) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_peserta_program FOREIGN KEY (id_program)
    REFERENCES program(id_program) ON UPDATE CASCADE,
  CONSTRAINT fk_peserta_instruktur FOREIGN KEY (id_instruktur)
    REFERENCES instruktur(id_instruktur) ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS seleksi (
  id_seleksi      VARCHAR(10) NOT NULL PRIMARY KEY,
  id_peserta      VARCHAR(10) NOT NULL,
  jenis_seleksi   VARCHAR(100),
  tanggal_seleksi VARCHAR(10),            -- sengaja VARCHAR dulu agar format tanggal di kode tidak berubah
  nilai           INT NOT NULL DEFAULT 0,
  status_hasil    VARCHAR(30),
  CONSTRAINT fk_seleksi_peserta FOREIGN KEY (id_peserta)
    REFERENCES peserta(id_peserta) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS dokumen (
  id_dokumen        VARCHAR(10) NOT NULL PRIMARY KEY,
  id_peserta        VARCHAR(10) NOT NULL UNIQUE,   -- 1 peserta = 1 baris dokumen keberangkatan
  tanggal_berangkat VARCHAR(10),
  no_paspor         VARCHAR(30),
  file_paspor       VARCHAR(255),                  -- path berkas di disk
  hasil_mcu         VARCHAR(100),
  file_mcu          VARCHAR(255),
  no_visa           VARCHAR(30),
  file_visa         VARCHAR(255),
  nik_ktp           VARCHAR(16),
  file_ktp          VARCHAR(255),
  CONSTRAINT fk_dokumen_peserta FOREIGN KEY (id_peserta)
    REFERENCES peserta(id_peserta) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ================= DATA AWAL (disalin dari DataStore lama) =================
-- Catatan:
--  * Password instruktur = ID instruktur, karena Instruktur.java memanggil super(username, idInstruktur, ...).
--  * Username peserta harus unik. Di data lama PST006-PST015 sama-sama "un"/"pas";
--    PST006 tetap "un", sisanya diganti pstNNN (password tetap "pas").
--  * PST011, PST014, PST015: status pembayaran di data lama tertulis "Lulus" (salah isi), diganti "Lunas".

INSERT IGNORE INTO pengguna (username, password, nama_lengkap, role) VALUES
 ('admin',            'admin123',    'Farel (Admin LPK)',          'ADMIN'),
 ('dian_p',           'INS001',      'Dian Priatna Kusuma, S.PD.', 'INSTRUKTUR'),
 ('yamada_s',         'INS002',      'Yamada Sensei',              'INSTRUKTUR'),
 ('zani',             'INS003',      'Utsukushii Zani',            'INSTRUKTUR'),
 ('supandi',          'INS004',      'Supandi S.S',                'INSTRUKTUR'),
 ('3208010205050001', 'yuukiPST001', 'Azril Algiffari',            'PESERTA'),
 ('tania',            'tania123',    'Tania Elliyanti',            'PESERTA'),
 ('akram',            'akram123',    'Akram Pratama Putra',        'PESERTA'),
 ('PST004',           'PST004',      'Muhamad Farel Fauzan',       'PESERTA'),
 ('novashabilla',     '180806',      'Nova Shabilla',              'PESERTA'),
 ('un',               'pas',         'Mikasa',                     'PESERTA'),
 ('pst007',           'pas',         'Faida Nur Mufidah',          'PESERTA'),
 ('pst008',           'pas',         'Rizky Aditya',               'PESERTA'),
 ('pst009',           'pas',         'Dewi Lestari',               'PESERTA'),
 ('pst010',           'pas',         'Ahmad Fauzi',                'PESERTA'),
 ('pst011',           'pas',         'Rina Melati',                'PESERTA'),
 ('pst012',           'pas',         'Dimas Anggara',              'PESERTA'),
 ('pst013',           'pas',         'Putri Maharani',             'PESERTA'),
 ('pst014',           'pas',         'Gilang Ramadhan',            'PESERTA'),
 ('pst015',           'pas',         'Ayu Wandira',                'PESERTA');

INSERT IGNORE INTO instruktur (id_instruktur, username, no_telp, spesialisasi, level_jlpt, kelas_diampu) VALUES
 ('INS001', 'dian_p',   '08123999748', 'Kaiwa',     'N2', 'Mandiri'),
 ('INS002', 'yamada_s', '08199999477', 'Kanji',     'N1', 'Reguler'),
 ('INS003', 'zani',     '0819900004',  'Budaya JP', 'N1', 'Reguler'),
 ('INS004', 'supandi',  '081994949',   'Budaya JP', 'N1', 'Reguler');

INSERT IGNORE INTO program (id_program, nama_program, bidang_usaha, biaya, kuota) VALUES
 ('PRG001', 'Pemagangan Manufaktur', 'Pabrik / Perakitan', 15000000, 30),
 ('PRG002', 'Pemagangan Caregiver',  'Kesehatan / Lansia',  12000000, 20),
 ('PRG003', 'Pemagangan Pertanian',  'Agrikultur',          10000000, 50);

INSERT IGNORE INTO peserta (id_peserta, username, nik, tanggal_lahir, jenis_kelamin, no_hp, agama, alamat,
  id_program, id_instruktur, level_bahasa, status_pembayaran, kelas, status_seleksi, status_keberangkatan) VALUES
 ('PST001','3208010205050001','3208010205050001','2002-05-05','Laki-laki', '0891234567', 'Islam','Kuningan', 'PRG001','INS001','N4','Lunas',      'Kelas A','Lulus',         'Siap Berangkat'),
 ('PST002','tania',           '3208013001060002','2003-01-30','Perempuan', '0897654321', 'Islam','Kuningan', 'PRG002','INS002','N4','Lunas',      'Kelas A','Lulus',         'Siap Berangkat'),
 ('PST003','akram',           '3208012008060003','2003-08-20','Laki-laki', '0812233445', 'Islam','Kuningan', 'PRG003','INS003','N3','Lunas',      'Kelas A','Gugur Permanen','Menunggu Dokumen'),
 ('PST004','PST004',          '3208017049510003','2003-12-17','Laki-laki', '0856677889', 'Islam','Kuningan', 'PRG001','INS001','N1','Lunas',      'Kelas A','Lulus',         'Belum Berangkat'),
 ('PST005','novashabilla',    '3208011234560005','2004-02-14','Perempuan', '0877112233', 'Islam','Kuningan', 'PRG002','INS003','N5','Belum Lunas','Kelas B','Belum Mulai',   'Belum Berangkat'),
 ('PST006','un',              '3208011102040001','2004-02-11','Laki-laki', '081122334455','Islam','Kuningan','PRG001','INS003','N5','Lunas',      'Kelas A','Lulus',         'Menunggu Dokumen'),
 ('PST007','pst007',          '3208012203050002','2005-03-22','Perempuan', '082233445566','Islam','Kuningan','PRG002','INS003','N4','Belum Lunas','Kelas A','Menunggu',      'Belum Berangkat'),
 ('PST008','pst008',          '3208011506030003','2003-06-15','Laki-laki', '083344556677','Islam','Cirebon', 'PRG001','INS003','N4','Lunas',      'Kelas B','Lulus',         'Siap Berangkat'),
 ('PST009','pst009',          '3208010809040004','2004-09-08','Perempuan', '084455667788','Islam','Kuningan','PRG003','INS003','N3','Lunas',      'Kelas B','Lulus',         'Siap Berangkat'),
 ('PST010','pst010',          '3208012512020005','2002-12-25','Laki-laki', '085566778899','Islam','Kuningan','PRG001','INS003','N5','Belum Lunas','Kelas A','Gugur Permanen','Batal Berangkat'),
 ('PST011','pst011',          '3208011402050006','2005-02-14','Perempuan', '086677889900','Islam','Kuningan','PRG002','INS003','N4','Lunas',      'Kelas A','Lulus',         'Menunggu Dokumen'),
 ('PST012','pst012',          '3208010307040007','2004-07-03','Laki-laki', '087788990011','Islam','Majalengka','PRG001','INS003','N5','Belum Lunas','Kelas B','Menunggu',      'Belum Berangkat'),
 ('PST013','pst013',          '3208012810050008','2005-10-28','Perempuan', '088899001122','Islam','Kuningan','PRG003','INS003','N3','Lunas',      'Kelas B','Lulus',         'Siap Berangkat'),
 ('PST014','pst014',          '3208011911030009','2003-11-19','Laki-laki', '089900112233','Islam','Kuningan','PRG002','INS003','N4','Lunas',      'Kelas A','Lulus',         'Belum Berangkat'),
 ('PST015','pst015',          '3208010701040010','2004-01-07','Perempuan', '081011223344','Islam','Cirebon', 'PRG001','INS003','N5','Lunas',      'Kelas B','Lulus',         'Menunggu Dokumen');

INSERT IGNORE INTO dokumen (id_dokumen, id_peserta, tanggal_berangkat, no_paspor, file_paspor, nik_ktp, file_ktp) VALUES
 ('DOK001','PST001','01/02/2026','B1234567','berkas_b1234567.pdf','3208123456780001','ktp_azril.jpg'),
 ('DOK002','PST002','01/02/2026','C9876543','berkas_c9876543.pdf',NULL,NULL),
 ('DOK003','PST003','—',NULL,NULL,NULL,NULL),
 ('DOK004','PST004','—',NULL,NULL,NULL,NULL);

INSERT IGNORE INTO seleksi (id_seleksi, id_peserta, jenis_seleksi, tanggal_seleksi, nilai, status_hasil) VALUES
 ('SEL-OLD1','PST003','Seleksi Berkas & Administrasi','20/09/2025',70,'Lulus'),
 ('SEL-OLD2','PST005','Seleksi Berkas & Administrasi','15/09/2025',80,'Lulus'),
 ('SEL-OLD3','PST005','Tes Fisik & Kesamaptaan',      '01/10/2025',60,'Tidak Lulus'),
 ('SEL001',  'PST001','Seleksi Bahasa Jepang',        '01/10/2025',85,'Lulus'),
 ('SEL002',  'PST002','Seleksi Wawancara User',       '01/10/2025',78,'Lulus'),
 ('SEL003',  'PST003','Seleksi Bahasa Jepang',        '05/10/2025',55,'Tidak Lulus'),
 ('SEL004',  'PST004','Seleksi Kemampuan Kerja',      '10/10/2025',92,'Lulus'),
 ('SEL005',  'PST005','Seleksi Bahasa Jepang',        '12/10/2025',40,'Gugur Permanen');
