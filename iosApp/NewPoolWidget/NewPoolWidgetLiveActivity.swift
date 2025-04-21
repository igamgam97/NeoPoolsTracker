//
//  NewPoolWidgetLiveActivity.swift
//  NewPoolWidget
//
//  Created by user278922 on 4/20/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import ActivityKit
import WidgetKit
import SwiftUI

struct NewPoolWidgetAttributes: ActivityAttributes {
    public struct ContentState: Codable, Hashable {
        // Dynamic stateful properties about your activity go here!
        var emoji: String
    }

    // Fixed non-changing properties about your activity go here!
    var name: String
}

struct NewPoolWidgetLiveActivity: Widget {
    var body: some WidgetConfiguration {
        ActivityConfiguration(for: NewPoolWidgetAttributes.self) { context in
            // Lock screen/banner UI goes here
            VStack {
                Text("Hello \(context.state.emoji)")
            }
            .activityBackgroundTint(Color.cyan)
            .activitySystemActionForegroundColor(Color.black)

        } dynamicIsland: { context in
            DynamicIsland {
                // Expanded UI goes here.  Compose the expanded UI through
                // various regions, like leading/trailing/center/bottom
                DynamicIslandExpandedRegion(.leading) {
                    Text("Leading")
                }
                DynamicIslandExpandedRegion(.trailing) {
                    Text("Trailing")
                }
                DynamicIslandExpandedRegion(.bottom) {
                    Text("Bottom \(context.state.emoji)")
                    // more content
                }
            } compactLeading: {
                Text("L")
            } compactTrailing: {
                Text("T \(context.state.emoji)")
            } minimal: {
                Text(context.state.emoji)
            }
            .widgetURL(URL(string: "http://www.apple.com"))
            .keylineTint(Color.red)
        }
    }
}

extension NewPoolWidgetAttributes {
    fileprivate static var preview: NewPoolWidgetAttributes {
        NewPoolWidgetAttributes(name: "World")
    }
}

extension NewPoolWidgetAttributes.ContentState {
    fileprivate static var smiley: NewPoolWidgetAttributes.ContentState {
        NewPoolWidgetAttributes.ContentState(emoji: "😀")
     }
     
     fileprivate static var starEyes: NewPoolWidgetAttributes.ContentState {
         NewPoolWidgetAttributes.ContentState(emoji: "🤩")
     }
}

#Preview("Notification", as: .content, using: NewPoolWidgetAttributes.preview) {
   NewPoolWidgetLiveActivity()
} contentStates: {
    NewPoolWidgetAttributes.ContentState.smiley
    NewPoolWidgetAttributes.ContentState.starEyes
}
