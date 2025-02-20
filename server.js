// const express = require('express');
// const bodyParser = require('body-parser');
// const nodemailer = require('nodemailer');
// const multer = require('multer');
// const db = require('./config/db');
// const cors = require('cors');

// const app = express();
// const upload = multer();

// // Middleware
// app.use(cors());
// app.use(bodyParser.urlencoded({ extended: true }));
// app.use(bodyParser.json());
// app.use(express.static('public'));

// // Save recipient email to database
// app.post('/save-email', (req, res) => {
//     const email = req.body.email;
//     db.query('DELETE FROM email_settings', (err) => {
//         if (err) {
//             console.error('Error clearing previous email settings:', err);
//             return res.status(500).send('Error saving email.');
//         }
//         db.query('INSERT INTO email_settings (recipient_email) VALUES (?)', [email], (err) => {
//             if (err) {
//                 console.error('Error inserting email:', err);
//                 return res.status(500).send('Error saving email.');
//             }
//             res.send('Email saved successfully.');
//         });
//     });
// });

// // Fetch recipient email from database
// app.get('/get-email', (req, res) => {
//     db.query('SELECT recipient_email FROM email_settings LIMIT 1', (err, results) => {
//         if (err) {
//             console.error('Error fetching email:', err);
//             return res.status(500).send('Error fetching email.');
//         }
//         if (results.length > 0) {
//             res.json({ email: results[0].recipient_email });
//         } else {
//             res.status(404).send('Email not set.');
//         }
//     });
// });

// // Send email with ZIP attachment
// app.post('/send-email', upload.single('zipFile'), (req, res) => {
//     const zipFile = req.file;
//     if (!zipFile) {
//         return res.status(400).json({ success: false, message: 'Missing file.' });
//     }

//     // Fetch recipient email from database
//     db.query('SELECT recipient_email FROM email_settings LIMIT 1', (err, results) => {
//         if (err) {
//             console.error('Error fetching email:', err);
//             return res.status(500).send('Error fetching email.');
//         }
//         if (results.length === 0) {
//             return res.status(404).json({ success: false, message: 'Recipient email not set.' });
//         }
        
//         const recipientEmail = results[0].recipient_email;
        
//         // Configure SMTP transporter
//         let transporter = nodemailer.createTransport({
//             service: 'Gmail',
//             auth: {
//                 user: process.env.EMAIL_USER,
//                 pass: process.env.EMAIL_PASS
//             }
//         });

//         // Email options
//         let mailOptions = {
//             from: 'Kok Siong',
//             to: recipientEmail,
//             subject: 'Selected EDI Files',
//             text: 'Please find the selected EDI files attached.',
//             attachments: [
//                 {
//                     filename: 'EDI_files.zip',
//                     content: zipFile.buffer
//                 }
//             ]
//         };

//         // Send email
//         transporter.sendMail(mailOptions, (error, info) => {
//             if (error) {
//                 console.error('Error sending email:', error);
//                 return res.status(500).json({ success: false, message: 'Error sending email: ' + error.message });
//             }
//             console.log('Email sent: ' + info.response);
//             res.status(200).json({ success: true, message: 'Email sent successfully.' });
//         });
//     });
// });

// // Start server
// const PORT = process.env.PORT || 3000;
// app.listen(PORT, () => {
//     console.log(`Server running on port ${PORT}`);
// });
