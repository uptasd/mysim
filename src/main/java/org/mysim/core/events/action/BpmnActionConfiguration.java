package org.mysim.core.events.action;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BpmnActionConfiguration {
   private String actionName;
   private List<String> subscribedContext;
   private List<String> publishedContext;
}

