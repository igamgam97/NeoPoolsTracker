//
//  NewPoolWidgetBundle.swift
//  NewPoolWidget
//
//  Created by user278922 on 4/20/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import WidgetKit
import SwiftUI

@main
struct NewPoolWidgetBundle: WidgetBundle {
    var body: some Widget {
        NeoPoolWidget()
        NewPoolWidgetControl()
        NewPoolWidgetLiveActivity()
    }
}
