# [App Name] – News Aggregator

An Android news aggregator that delivers real-time global headlines in one place. Users can browse by category, choose their preferred sources, and search for articles. Articles are cached locally so the app stays usable with a poor connection.

## Features
- **Category filtering**: browse news by topic (e.g., business, technology, sports, health).
- **Source selection**: choose which publishers to read.
- **Search**: find articles by keyword.
- **Offline support**: articles are persisted locally with Room.
- **Real-time data**: fresh headlines fetched from a RESTful news API.

## Tech stack
| Area | Technology |
|------|------------|
| Language | Kotlin |
| UI | XML layouts with ViewBinding |
| Networking | Retrofit ([+ OkHttp / Gson / Moshi]) |
| Local storage | Room |
| Architecture | Clean layered architecture ([MVVM], Repository pattern) |
| Async | [Kotlin Coroutines / Flow] |

## Architecture
The project follows a layered structure:

```
app/src/main/java/[your.package]/
├── data/
│   ├── remote/      # Retrofit API interface, DTOs
│   ├── local/       # Room database, DAOs, entities
│   └── repository/  # Single source of truth (remote + local)
├── domain/          # Models, use cases
└── ui/              # Activities/Fragments, ViewModels, adapters
```

The UI observes data from ViewModels, which request it from the repository. The repository decides whether to use the network or the Room cache.

## Getting started

### Requirements
- Android Studio (latest stable)
- JDK 17
- Android SDK (min API 24, target API 36)
- A free API key from [NewsAPI.org / your provider]

### Build and run
1. Clone the repository:
   ```bash
   git clone https://github.com/gwynplainee/AndroidFinalProject.git
   ```
2. Open the project in Android Studio and let Gradle sync.
3. Add your API key to `local.properties` (this file is git-ignored):
   ```properties
   NEWS_API_KEY=your_api_key_here
   ```
4. Run the `app` configuration on an emulator or a physical device.

## Sustainable Development Goal
This project supports **SDG 16: Peace, Justice and Strong Institutions** (target 16.10, public access to information) by making news from many sources easy to find and read, helping people stay informed. It also supports **SDG 4: Quality Education** by promoting access to information.

## Contributing
Contributions are welcome. Please read [CONTRIBUTING.md](CONTRIBUTING.md) and our [Code of Conduct](CODE_OF_CONDUCT.md) first.

## License
Released under the [Apache License 2.0](LICENSE).

## Ownership
Developed and maintained by gwynplaine and Aibek, students at [KBTU].

## Privacy and data
- The app requires no account and collects no personal data.
- Search queries and selected sources/categories are stored only on the device.
- Network requests go to [the news API provider] over HTTPS.
- No data is sold or shared with third parties, and no advertising or analytics SDKs are included.
- The API key is kept in `local.properties` and is never committed to the repository.

## Acknowledgements
News data provided by NewsAPI.org. Please follow their terms of use and attribution requirements.
