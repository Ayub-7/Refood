<template>
  <div id="tableContainer">
        <vs-row id="tableRow">
          <vs-table :data="tableData" style="border-spacing: 0px 20px; margin: 1em" search>
            <template slot="thead" style="background:blue">
              <vs-th sort-key="id" style="text-align: center;">
                <div>ID</div>
              </vs-th>
              <vs-th sort-key="title">
                <div>title</div>
              </vs-th >
              <vs-th sort-key="description">
                <div>Description</div>
              </vs-th>
              <vs-th sort-key="keywords">
                <div>Keywords</div>
              </vs-th>
              <vs-th><!-- Actions Column --></vs-th>
            </template>

            <template slot-scope="{data}">
              <vs-tr :key="item.id" v-for="item in data">
                <vs-td style="width: 20px; min-width: 100px; padding-right: 10px; text-align: center;">
                  <a href="#">{{ item.id }}</a>
                  <div>
                    <img id="marketImage" style="width: 100%; height: 100%; border-radius: 1em;" :src="require('../../public/ProductShoot.jpg')" />
                  </div>
                </vs-td>
                <vs-td>{{ item.title }} </vs-td>
                <vs-td>{{ item.description }} </vs-td>
                <vs-td>
                    <div id="cardKeywords"  v-for="keyword in item.keywords" :key="keyword.id" >#{{keyword.name}}</div>
                </vs-td>
                <td>
                  <!-- Effectively repeated above, should refactor at some point. -->
                </td>
              </vs-tr>
            </template>
          </vs-table>
        </vs-row>
  </div>
</template>

<script>
export default {
  props: ['tableData']

}
</script>

<style>

/* CARD STYLING */

#marketImage {
  width: 100%;
  height: auto;
}

#tableRow {
  display: block !important;
}

#cardKeywords {
  color: #1F74FF;
  font-size: 15px;
  padding: 2px;
  float: left;
}


/* REMOVE AUTO SCROLL HIDING, SO USER KNOWS IF PARAGRAPH IS LONGER THAN CARD SIZE */

::-webkit-scrollbar {
  -webkit-appearance: none;
  width: 5px;
}

th {
  background: #1F74FF;
  color: white;
}

vs-table {
  width: 100%;
}

.vs-con-table {
  background: none;
}

.vs-component.vs-con-table.vs-table-primary {
  margin: 0px !important;
}

::-webkit-scrollbar-thumb {
  border-radius: 4px;
  background-color: rgba(0, 0, 0, .5);
  box-shadow: 0 0 1px rgba(255, 255, 255, .5);
}


</style>
