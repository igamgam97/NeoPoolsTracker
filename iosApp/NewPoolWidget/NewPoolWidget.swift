import WidgetKit
import SwiftUI
import ComposeApp

struct Provider: TimelineProvider {
    func placeholder(in context: Context) -> SimpleEntry {
        SimpleEntry(date: Date(), hashrate: "Loading...", feeType: "Loading...", updated: "Loading...")
    }

    func getSnapshot(in context: Context, completion: @escaping (SimpleEntry) -> ()) {
        let entry = SimpleEntry(date: Date(), hashrate: "123.45", feeType: "PPLNS", updated: "today")
        completion(entry)
    }

    func getTimeline(in context: Context, completion: @escaping (Timeline<Entry>) -> ()) {
        let currentDate = Date()
        let refreshDate = Calendar.current.date(byAdding: .hour, value: 1, to: currentDate)!

        KoinInitializer().startKoin()
        let helper = KoinHelper()

        Task {
            do {
                let result = try await helper.getReward()

                let entry: SimpleEntry
                if let success = result as? PoolResult.Success {
                    let response = success.data
                    let updated = DateTimeUtilKt.formatTimestamp(timestamp: response.updated)
                    entry = SimpleEntry(
                        date: currentDate,
                        hashrate: String(response.data.hashrate1d),
                        feeType: response.data.feeType,
                        updated: updated
                    )
                } else if let failure = result as? PoolResult.Failure {
                    entry = SimpleEntry(
                        date: currentDate,
                        hashrate: "Error",
                        feeType: failure.message,
                        updated: "-"
                    )
                } else {
                    entry = SimpleEntry(
                        date: currentDate,
                        hashrate: "Unknown",
                        feeType: "Unknown",
                        updated: "-"
                    )
                }

                // Once data is processed, create the timeline and call the completion handler
                let timeline = Timeline(entries: [entry], policy: .after(refreshDate))
                completion(timeline)

            } catch {
                // Handle any errors that might occur during the async call
                let entry = SimpleEntry(
                    date: currentDate,
                    hashrate: "Error",
                    feeType: error.localizedDescription,
                    updated: "-"
                )
                let timeline = Timeline(entries: [entry], policy: .after(refreshDate))
                completion(timeline)
            }
        }
    }

}

struct SimpleEntry: TimelineEntry {
    let date: Date
    let hashrate: String
    let feeType: String
    let updated: String
}

struct NeoPoolWidgetEntryView : View {
    var entry: Provider.Entry
    @Environment(\.widgetFamily) var family

    var body: some View {
        ZStack {
            Color.black.edgesIgnoringSafeArea(.all)
            
            VStack(alignment: .leading, spacing: 8) {
                Text("NeoPool Stats")
                    .font(.headline)
                    .foregroundColor(.white)
                
                Spacer()
                
                HStack {
                    VStack(alignment: .leading) {
                        Text("Hashrate (1d):")
                            .font(.caption)
                            .foregroundColor(.gray)
                        Text(entry.hashrate)
                            .font(.body)
                            .foregroundColor(.white)
                    }
                    
                    Spacer()
                    
                    VStack(alignment: .trailing) {
                        Text("Fee Type:")
                            .font(.caption)
                            .foregroundColor(.gray)
                        Text(entry.feeType)
                            .font(.body)
                            .foregroundColor(.white)
                    }
                }
                
                Spacer()
                
                Text("Updated: \(entry.updated)")
                    .font(.caption)
                    .foregroundColor(.gray)
            }
            .padding()
        }
    }
}

struct NeoPoolWidget: Widget {
    let kind: String = "NeoPoolWidget"

    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: Provider()) { entry in
            NeoPoolWidgetEntryView(entry: entry)
        }
        .configurationDisplayName("NeoPool Stats")
        .description("Display NeoPool mining statistics")
        .supportedFamilies([.systemMedium, .systemLarge])
    }
}

struct NeoPoolWidget_Previews: PreviewProvider {
    static var previews: some View {
        NeoPoolWidgetEntryView(entry: SimpleEntry(date: Date(), hashrate: "123.45", feeType: "PPLNS", updated: "today"))
            .previewContext(WidgetPreviewContext(family: .systemMedium))
    }
}
