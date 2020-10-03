/*
 * This file is a part of BSL Language Server.
 *
 * Copyright © 2018-2020
 * Alexey Sosnoviy <labotamy@gmail.com>, Nikita Gryzlov <nixel2007@gmail.com> and contributors
 *
 * SPDX-License-Identifier: LGPL-3.0-or-later
 *
 * BSL Language Server is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * BSL Language Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with BSL Language Server.
 */
package com.github._1c_syntax.bsl.languageserver.diagnostics;

import com.github._1c_syntax.bsl.languageserver.context.symbol.MethodSymbol;
import com.github._1c_syntax.bsl.languageserver.context.symbol.Symbol;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticMetadata;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticSeverity;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticTag;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticType;
import com.github._1c_syntax.bsl.parser.BSLParser;
import com.github._1c_syntax.utils.CaseInsensitivePattern;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.regex.Pattern;

@DiagnosticMetadata(
  type = DiagnosticType.CODE_SMELL,
  severity = DiagnosticSeverity.MAJOR,
  minutesToFix = 1,
  tags = {
    DiagnosticTag.STANDARD
  }
)
public class NonExportMethodsInApiRegionDiagnostic extends AbstractVisitorDiagnostic {

  private static final Pattern REGION_NAME = CaseInsensitivePattern.compile(
    "^(?:ПрограммныйИнтерфейс|СлужебныйПрограммныйИнтерфейс|Public|Internal)$"
  );

  @Override
  public ParseTree visitSub(BSLParser.SubContext ctx) {

    documentContext.getSymbolTree().getMethodSymbol(ctx).ifPresent((MethodSymbol methodSymbol) -> {
      if (methodSymbol.isExport()) {
        return;
      }

      methodSymbol.getRootParent().ifPresent((Symbol rootRegion) -> {
        if (REGION_NAME.matcher(rootRegion.getName()).matches()) {
          String message = info.getMessage(methodSymbol.getName(), rootRegion.getName());
          diagnosticStorage.addDiagnostic(methodSymbol.getSubNameRange(), message);
        }
      });
    });

    return ctx;
  }

}
