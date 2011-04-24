class CreateMaterials < ActiveRecord::Migration
  def self.up
    create_table :materials do |t|
      t.string :catalog_id
      t.text :description
      t.string :dimensions
      t.double :price
      t.datetime :valid_until

      t.timestamps
    end
  end

  def self.down
    drop_table :materials
  end
end
