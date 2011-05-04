class CreateMaterials < ActiveRecord::Migration
  def self.up
    create_table :materials do |t|
      t.references :material
      t.string :catalog_number
      t.text :description
      t.string :dimensions
      t.decimal :price, :precision => 14, :scale => 2
      t.datetime :valid_until
      
      t.timestamps
    end
  end

  def self.down
    drop_table :materials
  end
end
