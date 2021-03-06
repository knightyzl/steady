/**
 * This file is part of Eclipse Steady.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Copyright (c) 2018-2020 SAP SE or an SAP affiliate company and Eclipse Steady contributors
 */
package org.eclipse.steady.shared.json.model;

import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.eclipse.steady.shared.enums.Scope;

public class ExemptionScope implements IExemption {

  /**
   * Deprecated configuration setting <code>REP_EXCL_UNASS="vulas.report.exceptionScopeBlacklist"</code>.
   */
  public static final String DEPRECATED_CFG = "vulas.report.exceptionScopeBlacklist";

  /** Deprecated configuration key in backend. **/
  public static final String DEPRECATED_KEY_BACKEND = "report.exceptionScopeBlacklist";

  /**
   * New configuration setting <code>REP_EXCL_UNASS="vulas.report.exemptScope"</code>.
   */
  public static final String CFG = "vulas.report.exemptScope";

  private Scope scope = null;

  public ExemptionScope(Scope _scope) {
    this.scope = _scope;
  }

  @Override
  public boolean isExempted(VulnerableDependency _vd) {
    final boolean is_exempted = this.scope.equals(_vd.getDep().getScope());
    return is_exempted;
  }

  @Override
  public String getReason() {
    return "Vulnerable dependencies with scope ["
        + this.scope
        + "] are exempted through configuration settings ["
        + CFG
        + "] or ["
        + DEPRECATED_CFG
        + "] (deprecated)";
  }

  /**
   * Reads the {@link Configuration} setting {@link #CFG} (if any) in order to create one or more {@link ExemptionScope}s.
   * Also considers the deprecated setting {@link #DEPRECATED_CFG}.
   *
   * @param _map
   * @return
   */
  public static ExemptionSet readFromConfiguration(Configuration _map) {
    final ExemptionSet exempts = new ExemptionSet();

    // Deprecated setting
    String[] scopes = _map.getStringArray(DEPRECATED_CFG);
    if (scopes != null && scopes.length > 0) {
      for (Scope s : Scope.fromStringArray(scopes)) {
        exempts.add(new ExemptionScope(s));
      }
    }

    // New setting
    scopes = _map.getStringArray(CFG);
    if (scopes != null && scopes.length > 0) {
      for (Scope s : Scope.fromStringArray(scopes)) {
        exempts.add(new ExemptionScope(s));
      }
    }

    return exempts;
  }

  /**
   * Reads the configuration setting {@link #DEPRECATED_CFG} and {@link #CFG} (if any) in order to create one or more {@link ExemptionScope}s.
   *
   * @param _cfg
   * @return
   */
  public static ExemptionSet readFromConfiguration(Map<String, String> _map) {
    final ExemptionSet exempts = new ExemptionSet();

    // Deprecated setting
    if (_map.containsKey(DEPRECATED_CFG)) {
      final String[] scopes = _map.get(DEPRECATED_CFG).split(",");
      if (scopes != null && scopes.length > 0) {
        for (Scope s : Scope.fromStringArray(scopes)) {
          exempts.add(new ExemptionScope(s));
        }
      }
    }

    // Deprecated key value from backend (to support backward compatibility with results already
    // existing in backend for apps that scanned with client versions <3.1.12)
    if (_map.containsKey(DEPRECATED_KEY_BACKEND)) {
      final String[] scopes = _map.get(DEPRECATED_KEY_BACKEND).split(",");
      if (scopes != null && scopes.length > 0) {
        for (Scope s : Scope.fromStringArray(scopes)) {
          exempts.add(new ExemptionScope(s));
        }
      }
    }

    // New setting
    if (_map.containsKey(CFG)) {
      final String[] scopes = _map.get(CFG).split(",");
      if (scopes != null && scopes.length > 0) {
        for (Scope s : Scope.fromStringArray(scopes)) {
          exempts.add(new ExemptionScope(s));
        }
      }
    }

    return exempts;
  }

  @Override
  public String toString() {
    return "Exemption [scope=" + this.scope + "]";
  }

  public String toShortString() {
    return this.scope.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((scope == null) ? 0 : scope.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    ExemptionScope other = (ExemptionScope) obj;
    if (scope != other.scope) return false;
    return true;
  }
}
