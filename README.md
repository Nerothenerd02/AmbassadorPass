AmbassadorPass

Decentralized, Ambassador-Driven Ticketing System for Exclusive Events

AmbassadorPass is a modern mobile-first ticketing system designed for exclusive low-to-mid-scale events. Unlike traditional ticketing platforms, it rewards ambassadors who promote events by giving them commissions on successful invites. The app provides admins with full event management capabilities, ambassadors with performance tracking, and casual users with a seamless ticket purchasing experience.

✨ Features

Admin Dashboard

Create/manage parties with details (name, date, location, price, ambassador markup).

Upload multiple venue images stored in Firebase Storage.

Assign ambassadors and generate unique PartyLinks.

Track ambassador performance and attendee registration.

Ambassador Dashboard

View assigned parties.

Track invitations, registrations, and earnings in real time.

Share unique PartyLinks with attendees.

Casual User Experience

Enter PartyLink to view venue slideshow & party details.

Register and (simulated) purchase tickets.

Receive digital tickets with event info and location.

Media & Branding

Engaging 7-second video loop on the main screen (via ExoPlayer).

Venue image slideshow for immersive previews.

Clean black-and-red design with consistent branding.

Typewriter text effects to incentivize ticket purchases.

🛠️ Tech Stack

Language: Kotlin

Architecture: MVVM (Model-View-ViewModel)

Backend: Firebase (Firestore, Authentication, Storage)

UI/UX: ExoPlayer, Glide, Typewriter animation, Black/Red theme

📂 Project Structure

AmbassadorPass/
│── app/                # Android app source code
│── data/               # Repositories (Firebase interaction logic)
│── model/              # Core entities (User, Ambassador, Party)
│── view/               # Activities & XML layouts
│── viewmodel/          # ViewModel classes managing UI state
│── assets/             # Videos, icons, and branding assets
│── README.md           # Project documentation

🚀 Getting Started

Clone the repository:
git clone https://github.com/Nerothenerd02/AmbassadorPass.git
cd AmbassadorPass
Open in Android Studio.

Run on an Android device or emulator.

🔑 Admin test credentials:

Email: elvistest@zohomail.com

Password: AdminTest

🧪 Testing & Validation

Unit Testing: Repository functions (e.g., validateKeycode) tested with valid/invalid inputs.

UI Testing: Verified error handling, ambassador assignment, and user registration flows.

Stress Testing: Simulated multiple requests — Firebase backend handled loads efficiently.

Security: Firebase rules implemented to restrict unauthorized access to admin/ambassador data.

⚡ Challenges & Learnings

Handling unstable ExoPlayer APIs across devices.

Designing robust Firebase security rules for role-based access.

Managing dynamic data flow between ViewModels and repositories.

Querying complex Firebase datatypes (dates, arrays) efficiently.

📖 References

This project was developed as part of Mobile Development coursework at the University of Nottingham Malaysia.

👨‍💻 Author

Elvis Musungu Simiyu
Computer Science with Artificial Intelligence, University of Nottingham

GitHub: @Nerothenerd02

