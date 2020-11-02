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

import com.github._1c_syntax.bsl.languageserver.context.DocumentContext;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticMetadata;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticScope;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticSeverity;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticTag;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticType;
import com.github._1c_syntax.bsl.parser.BSLParser;
import com.github._1c_syntax.mdclasses.metadata.additional.MDOType;
import com.github._1c_syntax.mdclasses.metadata.additional.ModuleType;
import com.github._1c_syntax.utils.CaseInsensitivePattern;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp4j.Diagnostic;

import java.util.List;
import java.util.regex.Pattern;

@DiagnosticMetadata(
  type = DiagnosticType.CODE_SMELL,
  severity = DiagnosticSeverity.INFO,
  minutesToFix = 1,
  modules = {
    ModuleType.CommonModule,
    ModuleType.ObjectModule,
    ModuleType.ManagerModule,
    ModuleType.FormModule,
    ModuleType.RecordSetModule
  },
  tags = {
    DiagnosticTag.STANDARD,
    DiagnosticTag.CLUMSY
  },
  scope = DiagnosticScope.BSL
)
public class RedundantAccessToObjectDiagnostic extends AbstractVisitorDiagnostic {

  private static final Pattern PATTERN = CaseInsensitivePattern.compile("^ЭтотОбъект|ThisObject");
  private static final Pattern PATTERN_WITH_DOT = CaseInsensitivePattern.compile("^(ЭтотОбъект|ThisObject)\\..*");
  private boolean needCheckName = false;
  private boolean skipLValue = false;
  private Pattern namePattern;
  private Pattern namePatternWithDot;

  @Override
  public List<Diagnostic> getDiagnostics(DocumentContext documentContext) {
    var typeModule = documentContext.getModuleType();
    if (typeModule != ModuleType.ObjectModule && typeModule != ModuleType.FormModule) {
      documentContext.getMdObject().ifPresent(mdObjectBase -> {
        needCheckName = true;
        skipLValue = true;
        namePattern = CaseInsensitivePattern.compile(String.format("^%s", mdObjectBase.getName()));
        namePatternWithDot = CaseInsensitivePattern.compile(
          String.format(getManagerModuleName(mdObjectBase.getType()), mdObjectBase.getName())
        );
      });
    }
    return super.getDiagnostics(documentContext);
  }

  @Override
  public ParseTree visitCallStatement(BSLParser.CallStatementContext ctx) {
    if (ctx.globalMethodCall() != null && ctx.getStart() == ctx.globalMethodCall().getStart()) {
      return super.visitCallStatement(ctx);
    }

    if (PATTERN_WITH_DOT.matcher(ctx.getText()).matches()) {
      diagnosticStorage.addDiagnostic(ctx.getStart());
    }

    if (needCheckName && namePatternWithDot.matcher(ctx.getText()).matches()) {
      diagnosticStorage.addDiagnostic(ctx.getStart());
    }

    return super.visitCallStatement(ctx);
  }

  @Override
  public ParseTree visitComplexIdentifier(BSLParser.ComplexIdentifierContext ctx) {
    var identifier = ctx.IDENTIFIER();
    var modifiers = ctx.modifier();

    if (identifier == null || modifiers.size() == 0) {
      return ctx;
    }

    if (PATTERN.matcher(identifier.getText()).matches() && modifiers.get(0) != null) {
      diagnosticStorage.addDiagnostic(ctx.getStart());
    }

    if (needCheckName && namePattern.matcher(identifier.getText()).matches() && modifiers.get(0) != null) {
      diagnosticStorage.addDiagnostic(ctx.getStart());
    }

    return ctx;
  }

  @Override
  public ParseTree visitLValue(BSLParser.LValueContext ctx) {
    if (skipLValue) {
      return ctx;
    }

    var identifier = ctx.IDENTIFIER();
    var acceptor = ctx.acceptor();

    if (identifier == null || acceptor == null) {
      return ctx;
    }

    if (PATTERN.matcher(identifier.getText()).matches() && acceptor.accessProperty() != null) {
      diagnosticStorage.addDiagnostic(ctx.getStart());
    }

    if (needCheckName && namePattern.matcher(identifier.getText()).matches() && acceptor.accessProperty() != null) {
      diagnosticStorage.addDiagnostic(ctx.getStart());
    }

    return ctx;
  }

  private static String getManagerModuleName(MDOType objectType) {
    if (objectType == MDOType.CATALOG) {
      return "^(Справочники|Catalogs)\\.%s\\..*";
    } else if (objectType == MDOType.DOCUMENT) {
      return "^(Документы|Documents)\\.%s\\..*";
    } else if (objectType == MDOType.ACCOUNTING_REGISTER) {
      return "^(РегистрыБухгалтерии|AccountingRegisters)\\.%s\\..*";
    } else if (objectType == MDOType.ACCUMULATION_REGISTER) {
      return "^(РегистрыНакопления|AccumulationRegisters)\\.%s\\..*";
    } else if (objectType == MDOType.CALCULATION_REGISTER) {
      return "^(РегистрыРасчета|CalculationRegisters)\\.%s\\..*";
    } else if (objectType == MDOType.INFORMATION_REGISTER) {
      return "^(РегистрыСведений|InformationRegisters)\\.%s\\..*";
    } else {
      return "^%s\\..*";
    }
  }
}
