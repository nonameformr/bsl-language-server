package org.github._1c_syntax.bsl.languageserver.diagnostics;

import org.eclipse.lsp4j.Diagnostic;
import org.github._1c_syntax.bsl.languageserver.utils.RangeHelper;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommentedCodeDiagnosticTest extends AbstractDiagnosticTest<CommentedCodeDiagnostic> {

  private CommentedCodeDiagnosticTest() {
    super(CommentedCodeDiagnostic.class);
  }

  @Test
  void runTest()
  {
    List<Diagnostic> diagnostics = getDiagnostics();

    assertThat(diagnostics).hasSize(2);
    assertThat(diagnostics.get(0).getRange()).isEqualTo(RangeHelper.newRange(4, 8, 4, 20));
    assertThat(diagnostics.get(1).getRange()).isEqualTo(RangeHelper.newRange(10, 0, 10, 13));
  }
}