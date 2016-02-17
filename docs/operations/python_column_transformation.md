---
layout: documentation
displayTitle: Python Column Transformation
title: Python Column Transformation
description: Python Column Transformation
usesMathJax: true
includeOperationsMenu: true
---

Executes Python function provided by the user on a column (columns) of [DataFrame](../classes/dataframe.html) connected to its input port.
Returns modified `DataFrame`.

Also returns a [Transformer](../classes/transformer.html) that can be later applied
to another `DataFrame` with a [Transform](transform.html) operation.

The function that will be executed has to:

* have name <code>transform_value</code>,

* take exactly two arguments: value to be transformed and the name of column currently being transformed,

* return transformed value that conforms with selected target type (parameter).

#### Example Python code:
{% highlight python %}
def transform_value(value, column_name):
    return value
{% endhighlight %}


**Since**: Seahorse 1.0.0

## Input

<table>
<thead>
<tr>
<th style="width:15%">Port</th>
<th style="width:15%">Type Qualifier</th>
<th style="width:70%">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td><code>0</code></td>
<td><code><a href="../classes/dataframe.html">DataFrame</a></code></td>
<td>DataFrame to be transformed.</td>
</tr>
</tbody>
</table>

## Output

<table>
<thead>
<tr>
<th style="width:15%">Port</th>
<th style="width:15%">Type Qualifier</th>
<th style="width:70%">Description</th>
</tr>
</thead>
<tbody>
    <tr>
      <td><code>0</code></td>
      <td><code><a href="../classes/dataframe.html">DataFrame</a></code></td>
      <td>Output DataFrame</td>
    </tr>
    <tr>
      <td><code>1</code></td>
      <td><code><a href="../classes/transformer.html">Transformer</a></code></td>
      <td>Transformer that allows to apply the operation on other DataFrames using <a href="transform.html">Transform</a></td>
    </tr>
</tbody>
</table>


## Parameters

<table class="table">
<thead>
<tr>
<th style="width:15%">Name</th>
<th style="width:15%">Type</th>
<th style="width:70%">Description</th>
</tr>
</thead>
<tbody>
<tr>
  <td><code>code</code></td>
  <td><code><a href="../parameter_types.html#code-snippet">Code Snippet</a></code></td>
  <td>The Python code to be executed. It has to contain Python function complying to signature presented in the operation's description.</td>
</tr>

<tr>
  <td><code>target type</code></td>
  <td><code><a href="../parameter_types.html#single-choice">Choice</a></code></td>
  <td>Target type of the conversion. Possible values are: <code>[String, Boolean, Timestamp, Double, Float, Long, Integer, Vector]</code>.</td>
</tr>

<tr>
  <td><code>operate on</code></td>
  <td><code><a href="../parameter_types.html#input-output-column-selector">InputOutputColumnSelector</a></code></td>
  <td>Input and output columns for the operation.</td>
</tr>
</tbody>
</table>